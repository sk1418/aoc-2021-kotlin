// https://adventofcode.com/2021/day/10
fun main() {
    val today = "Day10"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    part1(testInput).also {
        println("TEST part1: $it")
        check(it == 0)
    }
    println("part1: ${part1(input)}")

    part2(testInput).also {
        println("TEST part2: $it")
        check(it == 0)
    }
    println("part2: ${part2(input)}")
}
