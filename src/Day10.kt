// https://adventofcode.com/2021/day/10
fun main() {
    val today = "Day10"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        return input.sumOf { l -> NavLineParser(l).calcCorruptedValue() }
    }

    fun part2(input: List<String>): Long {
        return input.map { l -> NavLineParser(l).calcInCompleteValue() }
            .filter { it > 0 }.sorted().let { it[it.size / 2] }
    }

    chkTestInput(part1(testInput), 26397L, Part1)
    println("$Part1: ${part1(input)}")

    chkTestInput(part2(testInput), 288957L, Part2)
    println("$Part2: ${part2(input)}")
}

data class NavLineParser(val line: String) {
    private val seq = line.asSequence()

    private val stack = ArrayDeque<Pair<Char, Char>>()
    private fun <T> ArrayDeque<T>.push(item: T) = this.addLast(item)
    private fun <T> ArrayDeque<T>.pop() = this.removeLast()

    private val p1 = '(' to ')'
    private val p2 = '[' to ']'
    private val p3 = '{' to '}'
    private val p4 = '<' to '>'
    private val corruptedMap = mapOf(p1 to 3L, p2 to 57L, p3 to 1197L, p4 to 25137L)
    private val incompleteMap = mapOf(p1 to 1L, p2 to 2L, p3 to 3L, p4 to 4L)

    private val closings = corruptedMap.keys.map { it.second }
    private fun charToPair(c: Char) =
        corruptedMap.keys.let { pairs -> pairs.firstOrNull { it.first == c } ?: pairs.first { it.second == c } }

    fun calcCorruptedValue(): Long {
        seq.forEach {
            val v = chkCorrupted(it)
            if (v > 0) return v
        }
        return 0
    }

    fun calcInCompleteValue(): Long {
        seq.forEach { if (chkCorrupted(it) > 0) return 0L } //skip corrupted
        return if (stack.isNotEmpty()) {
            stack.map { incompleteMap[it]!! }.foldRight(0) { v, acc -> acc * 5 + v }
        } else 0
    }

    private fun chkCorrupted(c: Char): Long {
        val pair = charToPair(c)
        return if (c in closings) {
            if (pair == stack.pop()) 0 else corruptedMap[pair]!!
        } else { //opening
            0L.also { stack.push(pair) }
        }
    }
}
