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
        part1_1(testInput).also {
            println("TEST part1_1: $it")
            check(it == 37)
        }
        println("part1_1: ${part1_1(input)}")
        println()
        part2_1(testInput).also {
            println("TEST part1_2: $it")
            check(it == 168)
        }
        println("part2_1: ${part2_1(input)}")
    }.also { println("::: Way1 elapsed: $it ms") }

    println("\n>> a way faster <<\n")
    measureTimeMillis {
        part1_2(testInput).also {
            println("TEST part1_2: $it")
            check(it == 37)
        }
        println("part1_2: ${part1_2(input)}")
        println()
        part2_2(testInput).also {
            println("TEST part2_2: $it")
            check(it == 168)
        }
        println("part2_2: ${part2_2(input)}")
    }.also { println("::: Way2 elapsed: $it ms") }
}
