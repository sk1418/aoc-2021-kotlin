import java.awt.print.Paper

// https://adventofcode.com/2021/day/13
fun main() {
    val today = "Day13"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        return Paper(input).also { it.applyFold(1) }.countVisible().toLong()
    }

    fun part2(input: List<String>): Long {
        Paper(input).also { it.applyFold() }.printMatrix()
        return 0
    }

    chkTestInput(part1(testInput), 17L, Part1)
    println("$Part1: ${part1(input)}")

    chkTestInput(part2(testInput), 0L, Part2)
    println("$Part2: ${part2(input)}")
}

data class Paper(val input: List<String>) {
    private var matrix: Array<IntArray>
    private var foldRules: List<Pair<String, Int>>
    private var maxX = 0
    private var maxY = 0

    init { // parsing input
        val (pos, folds) = input.fold(mutableListOf<Pair<Int, Int>>() to mutableListOf<Pair<String, Int>>()) { (p, f), e ->
            (p to f).apply {
                with(e) {
                    when {
                        contains(",") -> p += e.split(",").let { it[0].toInt() to it[1].toInt() }
                        startsWith("fold along") -> f += e.split(" ")[2].split("=").let { it[0] to it[1].toInt() }
                    }
                }
            }
        }
        foldRules = folds
        maxX = pos.maxOf { it.first }
        maxY = pos.maxOf { it.second }
        matrix = Array(maxY + 1) { IntArray(maxX + 1) { 0 } }.apply { pos.forEach { this[it.second][it.first]++ } }
    }

    fun countVisible() = (0..maxY).sumOf { y ->
        (0..maxX).count { x -> matrix[y][x] > 0 }
    }

    fun applyFold(foldSteps: Int = -1) {
        foldRules.take(if (foldSteps > 0) foldSteps else foldRules.size).forEach { r ->
            when (r.first) {
                "x" -> doXFold(r.second)
                "y" -> doYFold(r.second)
            }
        }
    }

    fun printMatrix() = (0..maxY).forEach { y ->
        (0..maxX).forEach { x -> print(if (matrix[y][x] > 0) "#" else " ") }
        println()
    }

    private fun doXFold(xIdx: Int) {
        (0..maxY).forEach { y ->
            (xIdx + 1..maxX).forEach { x -> matrix[y][2 * xIdx - x] += matrix[y][x] }
        }
        maxX = xIdx - 1
    }

    private fun doYFold(yIdx: Int) {
        (yIdx + 1..maxY).forEach { y ->
            (0..maxX).forEach { x -> matrix[2 * yIdx - y][x] += matrix[y][x] }
        }
        maxY = yIdx - 1
    }
}