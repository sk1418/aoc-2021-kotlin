// https://adventofcode.com/2021/day/20
fun main() {
    val today = "Day20"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toImage(input: List<String>) =
        Day20Image((2..input.lastIndex)
            .map { input[it].toMutableList().map { c -> '#' == c }.toMutableList() }.toMutableList(),
            input[0].asSequence().map { it == '#' }.toMutableList())


    fun part1(input: List<String>): Long {
        return toImage(input).enhance(2).let { img -> img.outputMatrix.sumOf { l -> l.count { it } } }.toLong()
    }

    fun part2(input: List<String>): Long {
        return toImage(input).enhance(50).let { img -> img.outputMatrix.sumOf { l -> l.count { it } } }.toLong()
    }

    chkTestInput(part1(testInput), 35L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 3351L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class Day20Image(val origMatrix: MutableList<MutableList<Boolean>>, val dict: MutableList<Boolean>) {
    private var inputMatrix: MutableList<MutableList<Boolean>> = origMatrix
    var outputMatrix: MutableList<MutableList<Boolean>> = mutableListOf()

    private var infinityValue = false //init: dark (false)

    private fun MutableList<MutableList<Boolean>>.at(x: Int, y: Int) =
        if (x in this.indices && y in this[0].indices) this[x][y] else infinityValue


    fun enhance(rounds: Int) = this.apply {
        repeat(rounds) {
        if (outputMatrix.isNotEmpty()) inputMatrix = outputMatrix
                extendInOutput() //first extend the input image
            inputMatrix.forEachIndexed { x, list ->
                list.forEachIndexed { y, _ -> outputMatrix[x][y] = dict[getDictIdx(x, y)] }
            }
            if (dict[0]) infinityValue = !infinityValue
        }
    }

    private fun extendInOutput() {
        val tmp = MutableList(inputMatrix.size + 4) { MutableList(inputMatrix[0].size + 4) { infinityValue } }
        inputMatrix.forEachIndexed { x, list ->
            list.forEachIndexed { y, _ -> tmp[x + 2][y + 2] = inputMatrix[x][y] }
        }
        inputMatrix = tmp
        outputMatrix = inputMatrix.map { a -> a.map { infinityValue }.toMutableList() }.toMutableList()
    }

    private fun getDictIdx(x: Int, y: Int): Int =
        (x - 1..x + 1).map { i ->
            (y - 1..y + 1).map { j -> inputMatrix.at(i, j) }
        }.flatten().joinToString("") { if (it) "1" else "0" }.toInt(2)
}

