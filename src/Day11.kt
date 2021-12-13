// https://adventofcode.com/2021/day/7
fun main() {
    val today = "Day11"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        val oMatrix = OctopusMatrix(input)
        repeat(100) {
            oMatrix.take1Step()
        }
        return oMatrix.flashTimes
    }

    fun part2(input: List<String>): Long {
        val oMatrix = OctopusMatrix(input)
        var step = 0L
        while (!oMatrix.allFlashed()) {
            step++
            oMatrix.take1Step()
        }
        return step
    }

    chkTestInput(part1(testInput), 1656L, Part1)
    println("$Part1: ${part1(input)}")

    chkTestInput(part2(testInput), 195L, Part2)
    println("$Part2: ${part2(input)}")
}

data class OctopusMatrix(val input: List<String>) {
    private val matrix: List<MutableList<Int>> = input.map { it.map { c -> c.digitToInt() }.toMutableList() }
    private val maxI = matrix.size - 1
    private val maxJ = matrix[0].size - 1
    var flashTimes = 0L

    //extension functions
    private fun Pair<Int, Int>.outOfBound() = first < 0 || first > maxI || second < 0 || second > maxJ

    fun allFlashed() = matrix.all { r -> r.all { it == 0 } }

    fun take1Step() {
        (0..maxI).forEach { i -> (0..maxJ).forEach { j -> matrix[i][j]++ } }
        matrix.forEachIndexed { i, row -> row.forEachIndexed { j, v -> if (v > 9) flash(i, j) } }
    }

    private fun flash(i: Int, j: Int) {
        flashTimes++
        matrix[i][j] = 0
        findAdjacent(i, j).forEach { (i, j) ->
            if (matrix[i][j] != 0) {
                matrix[i][j]++.takeIf { matrix[i][j] > 9 }?.apply { flash(i, j) }
            }
        }
    }

    private fun findAdjacent(i: Int, j: Int): List<Pair<Int, Int>> = setOf(
        i to j - 1, i to j + 1, i - 1 to j, i + 1 to j, //L,R,U,D
        i - 1 to j - 1, i - 1 to j + 1, i + 1 to j + 1, i + 1 to j - 1 // diagonal(4)
    ).filterNot { it.outOfBound() }
}
