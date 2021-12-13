import kotlin.math.abs
import kotlin.math.ceil
import kotlin.system.measureTimeMillis

// https://adventofcode.com/2021/day/7
fun main() {
    val today = "Day07"

    val input = readInput(today)[0].split(",").map { it.toInt() }
    val testInput = readTestInput(today)[0].split(",").map { it.toInt() }

    fun calcCost(input: List<Int>, calcMethod: (Int, Int) -> Int): Int {
        val min = input.minOrNull() ?: 0
        val max = input.maxOrNull() ?: 0
        return (min..max).minOfOrNull { distance ->
            input.sumOf { calcMethod(it, distance) }
        } ?: 0
    }

    fun part1_1(input: List<Int>): Int {
        return calcCost(input) { position, distance -> abs(position - distance) }
    }

    fun part2_1(input: List<Int>): Int {
        return calcCost(input) { position, distance -> abs(position - distance).let { (1 + it) * it / 2 } }
    }

    fun part1_2(input: List<Int>): Int {
        val median = input.sorted().let { it[it.size / 2] }
        return input.sumOf { abs(it - median) }
    }

    fun part2_2(input: List<Int>): Int {
        val avg = ceil(input.average()).toInt()
        return input.sumOf { abs(it - avg).let { d -> (1 + d) * d / 2 } }
    }

    measureTimeMillis {
        chkTestInput(part1_1(testInput), 37, "Part 1-1")
        println("Part 1-1: ${part1_1(input)}")

        chkTestInput(part2_1(testInput), 168, "Part 2-1")
        println("Part 2-1: ${part2_1(input)}")
    }.also { println("::: Way1 elapsed: $it ms") }

    println("\n>> a way faster <<\n")
    measureTimeMillis {
        chkTestInput(part1_2(testInput), 37, "Part 2-1")
        println("Part 1-2: ${part1_2(input)}")

        chkTestInput(part2_2(testInput), 168, "Part 2-2")
        println("Part 2-2: ${part2_2(input)}")
    }.also { println("::: Way2 elapsed: $it ms") }
}
