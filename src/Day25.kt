import Point.*

// https://adventofcode.com/2021/day/25
fun main() {
    val today = "Day25"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        return Day25Puzzle(input).also { it.startMoving() }.steps.toLong()
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    chkTestInput(part1(testInput), 58L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 0L, Part2)
    println("[Part2]: ${part2(input)}")
}

enum class Point { RIGHT, DOWN, EMPTY }

data class Day25Puzzle(val input: List<String>) {
    var steps: Int = 0
    private val maxX: Int = input.lastIndex
    private val maxY: Int = input[0].lastIndex
    private var matrix: Array<Array<Point>> = Array(maxX + 1) { Array(maxY + 1) { EMPTY } }

    private fun Pair<Int, Int>.above() =
        (if (first == 0) maxX else first - 1) to second

    private fun Pair<Int, Int>.left() =
        first to (if (second == 0) maxY else second - 1)

    init {
        input.forEachIndexed { x, row ->
            row.toCharArray().forEachIndexed { y, c ->
                matrix[x][y] = when (c) {
                    '>' -> RIGHT
                    'v' -> DOWN
                    else -> EMPTY
                }
            }
        }
    }

    fun startMoving(): Long {
        while (true) {
            steps++
            if (oneMove() == 0) return steps + 1.toLong()
        }
    }

    private fun oneMove() = move(RIGHT) + move(DOWN)

    private fun move(sample: Point): Int {
        val swapMap = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        matrix.forEachIndexed { x, row ->
            row.forEachIndexed { y, p ->
                val (theX, theY) = (x to y).let { if (sample == RIGHT) it.left() else it.above() }
                if (p == EMPTY && matrix[theX][theY] == sample) {
                    swapMap[theX to theY] = x to y
                }
            }
        }
        return swapMap.onEach { (p1, p2) -> swapThem(p1, p2) }.size
    }

    private fun swapThem(p1: Pair<Int, Int>, p2: Pair<Int, Int>) {
        val t = matrix[p1.first][p1.second]
        matrix[p1.first][p1.second] = matrix[p2.first][p2.second]
        matrix[p2.first][p2.second] = t
    }
}