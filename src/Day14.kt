// https://adventofcode.com/2021/day/14
fun main() {
    val today = "Day14"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        return (Poly(input).also { p -> repeat(10) { p.insertOnce() } }).calcDiff()
    }

    fun part2(input: List<String>): Long {
        return (Poly(input).also { p -> repeat(40) { p.insertOnce() } }).calcDiff()
    }

    chkTestInput(part1(testInput), 1588L, Part1)
    println("$Part1: ${part1(input)}")

    chkTestInput(part2(testInput), 2188189693529L, Part2)
    println("$Part2: ${part2(input)}")
}

data class WinOf2Chars(val pair: Pair<Char, Char>) {
    fun insertAChar(aChar: Char): Pair<WinOf2Chars, WinOf2Chars> =
        WinOf2Chars(pair.first to aChar) to WinOf2Chars(aChar to pair.second)
}

data class InsertionRule(val pat: String, val insertion: Char) {
    val window = WinOf2Chars(pat[0] to pat[1])
}

data class Poly(val input: List<String>) {
    var template: String = input.first()
    private var rules: List<InsertionRule> = input.filter { "->" in it }
        .map { r -> r.split(" -> ").let { arr -> InsertionRule(arr[0], arr[1][0]) } }
    private var windows: Map<WinOf2Chars, Long> = template.windowed(2)
        .map { WinOf2Chars(it[0] to it[1]) }
        .groupingBy { it }.eachCount()
        .mapValues { it.value.toLong() }

    private fun findRule(win: WinOf2Chars) = rules.first { it.window == win }

    fun insertOnce() {
        windows = windows.flatMap { (win, cnt) ->
            findRule(win).let { r ->
                win.insertAChar(r.insertion).let { winPair -> listOf(winPair.first to cnt, winPair.second to cnt) }
            }
        }.groupingBy { it.first }.fold(0L) { acc, pair -> acc + pair.second }
    }

    fun statistic(): List<Pair<Char, Long>> {
        val charMap: MutableMap<Char, Long> =
            windows.flatMap { (w, c) -> listOf(w.pair.first to c, w.pair.second to c) }
                .groupingBy { it.first }
                .fold(0L) { acc, pair -> acc + pair.second }.toMutableMap()
        // first and last chars should be added to the result, so that: 2*Chars
        charMap[template.first()] = charMap[template.first()]!! + 1
        charMap[template.last()] = charMap[template.last()]!! + 1
        return charMap.mapValues { (_, v) -> v / 2 }.toList().sortedBy { it.second }
    }

    fun calcDiff() = statistic().let { it.last().second - it.first().second }.toLong()
}

