//https://adventofcode.com/2021/day/6

fun main() {
    val input = readInput("Day06")

    fun part1(input: List<String>): Int {
        var myList = input[0].split(",").map { it.toInt() }
        repeat(80) { _ ->
            val zeros = myList.count { it == 0 }
            myList = myList.map { if (it - 1 >= 0) it - 1 else 6 }.toMutableList()
                .also { it.addAll(MutableList(zeros) { 8 }) }
        }
        return myList.size
    }

    fun part2(input: List<String>): Long { //Long required
        var genMap =
            input[0].split(",").map { it.toInt() }.groupBy { it }.mapValues { (_, v) -> v.count().toLong() }
                .toMutableMap()
        repeat(256) {
            genMap = genMap.mapKeys { (k, _) -> k - 1 }.toMutableMap().also {
                it[8] = it.getOrDefault(-1, 0)
                it[6] = it.getOrDefault(-1, 0) + it.getOrDefault(6, 0)
                it.remove(-1)
            }
        }
        return genMap.values.sum()
    }

    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

