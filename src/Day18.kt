// https://adventofcode.com/2021/day/18
fun main() {
    val today = "Day18"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        return Day18Helper(input).sumAndCalcMagnitude().toLong()
    }

    fun part2(input: List<String>): Long {
        return Day18Helper(input).findMaxMagnitudeOfTwo().toLong()
    }

    chkTestInput(part1(testInput), 4140L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 3993L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class Day18Helper(val input: List<String>) {
    private val ps = 100  // '['
    private val pe = -100 // ']'
    private var snailFishNumberList = input.map { row ->
        SnailFishNumber(
            row.toList().map {
                when (it) {
                    '[' -> ps
                    ']' -> pe
                    ',' -> 999
                    else -> it.digitToInt()
                }
            }.filter { it < 999 }.toMutableList()
        )
    }

    fun sumAndCalcMagnitude(): Int {
        return snailFishNumberList.reduce { acc, e -> acc.addOther(e).also { it.doReduce() } }.also {
            println("after sum ${it.theNumList}".replace("-100", "]").replace("100", "["))
        }.magnitude()
    }

    fun findMaxMagnitudeOfTwo(): Int {
        val result = mutableListOf<Int>()
        snailFishNumberList.forEach { a ->
            snailFishNumberList.forEach { b ->
                if (a != b) {
                    result += a.addOther(b).also { it.doReduce() }.magnitude()
                }
            }
        }
        return result.maxOf { it }
    }
}

data class SnailFishNumber(var theNumList: MutableList<Int>, val ps: Int = 100, val pe: Int = -100) {
    private fun isRegularNum(n: Int) = n != ps && n != pe

    fun addOther(other: SnailFishNumber) = SnailFishNumber(
        listOf(mutableListOf(ps), this.theNumList, other.theNumList, mutableListOf(pe)).flatten().toMutableList())

    fun doReduce() {
        startExplode()
        startSplit()
    }

    fun magnitude(): Int {
        var rlist = theNumList
        while (rlist.size > 1) {
            val wlist = rlist
            rlist = mutableListOf()
            var i = 0
            while (i <= wlist.lastIndex) {
                if (i <= wlist.lastIndex - 3 && wlist[i] == ps && wlist[i + 3] == pe) {
                    rlist += (3 * wlist[i + 1] + 2 * wlist[i + 2])
                    i += 4
                } else {
                    rlist += wlist[i]
                    i++
                }
            }
        }
        return rlist[0]
    }

    private fun startExplode() {
        var psIdx = idxToExplode()
        while (psIdx != -1) {
            applyExplode(psIdx)
            psIdx = idxToExplode()
        }
    }

    private fun startSplit() {
        var idxSplit = idxToSplit()
        while (idxSplit != -1) {
            applySplit(idxSplit)
            startExplode()
            idxSplit = idxToSplit()
        }
    }

    private fun idxToExplode(): Int {
        var mark = 0
        for (i in theNumList.indices) {
            when (theNumList[i]) {
                ps -> {
                    mark += ps
                    if (mark == 500) return i
                }
                pe -> mark -= ps
            }
        }
        return -1
    }

    private fun applyExplode(psIdx: Int) {
        val ll = theNumList.subList(0, psIdx)
        val lm = theNumList.subList(psIdx, psIdx + 4)
        val lr = theNumList.subList(psIdx + 4, theNumList.lastIndex + 1)

        val (n1, n2) = lm[1] to lm[2]

        //add to the first left regular num
        for (i in ll.lastIndex downTo 0) {
            if (isRegularNum(ll[i])) {
                ll[i] += n1
                break
            }
        }
        //add to the next right regular num
        for (i in lr.indices) {
            if (isRegularNum(lr[i])) {
                lr[i] += n2
                break
            }
        }
        theNumList = listOf(ll, mutableListOf(0), lr).flatten().toMutableList() //concatenate all 3 parts
    }

    private fun idxToSplit(): Int {
        for (i in theNumList.indices)
            if (isRegularNum(theNumList[i]) && theNumList[i] > 9) return i
        return -1
    }

    private fun applySplit(idxSplit: Int) {
        val ll = theNumList.subList(0, idxSplit)
        val lm = mutableListOf(ps, theNumList[idxSplit] / 2, (theNumList[idxSplit] + 1) / 2, pe)
        val lr = theNumList.subList(idxSplit + 1, theNumList.lastIndex + 1)
        theNumList = listOf(ll, lm, lr).flatten().toMutableList()
    }
}
