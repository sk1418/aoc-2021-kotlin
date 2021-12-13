fun main() {
    val input = readInput("Day04")

    fun toBoards(theList: List<String>): MutableList<Board> {
        return theList.filterNot { it == "" }.chunked(5) {
            Board(it)
        }.toMutableList()
    }

    fun doJob(input: List<String>, part: String): Int {
        val theList = input.toMutableList()
        val seq = theList.removeAt(0).split(",")
        theList.removeAt(0)
        val boards = toBoards(theList)
        var result = -1
        run out@{
            seq.forEach seq@{ num ->
                boards.forEach board@{
                    val r = it.markNumber(num.toInt())
                    if (r != -1) {
//                    println("Num:$num, matrix Value:$r; result: ${num.toInt() * r}")
                        result = num.toInt() * r
                        if (part == Part1)
                            return@out
                    }
                }
            }
        }
        return result
    }

    fun part1(input: List<String>) = doJob(input, Part1)
    fun part2(input: List<String>) = doJob(input, Part2)

    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

data class Board(val fiveLines: List<String>) {
    private val matrix: Array<IntArray> = Array(5) { IntArray(5) }
    private val mRow: MutableMap<Int, MutableList<Int>> = mutableMapOf()
    private val mCol: MutableMap<Int, MutableList<Int>> = mutableMapOf()
    private var done = false

    init {
        fiveLines.forEachIndexed { idx, line ->
            matrix[idx] = line.trim().split(Regex(" +")).map { it.toInt() }.toIntArray()
        }
    }

    fun markNumber(num: Int): Int {
        if (done.not())
            run loop@{
                matrix.forEachIndexed { ir, arr ->
                    arr.forEachIndexed { ic, v ->
                        if (v == num) {
                            mRow.getOrPut(ir) { mutableListOf() }.add(ic)
                            mCol.getOrPut(ic) { mutableListOf() }.add(ir)
                            matrix[ir][ic] = -1
                            if (mRow[ir]!!.size == 5 || mCol[ic]!!.size == 5) {
                                done = true
                                return matrix.sumOf { it.filter { n -> n > 0 }.sum() }
                            }
                            return@loop
                        }
                    }
                }
            }
        return -1
    }
}

