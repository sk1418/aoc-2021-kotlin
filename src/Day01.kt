fun main() {
    val input = readInputAsInts("Day01")

    fun doJob(preX: Int): Int = (preX until input.size).count { input[it] > input[it - preX] }

    fun part1(input: List<Int>) = doJob(1)
    fun part2(input: List<Int>) = doJob(3)


    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
