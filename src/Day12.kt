// https://adventofcode.com/2021/day/12
fun main() {
    val today = "Day12"

    val input = readInput(today)
    val testInput = readTestInput(today)

    //extension fun
    fun String.isUpper() = this == this.uppercase()

    val lowerNotExist = { path: List<String>, target: String -> target.isUpper() || !path.contains(target) }
    val noTwiceLower = { path: List<String>, target: String ->
        lowerNotExist(path, target) ||
                path.filter { !it.isUpper() }.groupBy { it }.values.none { it.size == 2 }
    }

    fun part1(input: List<String>): Long {
        val graph = TheGraph(input).also { it.startTraversing(lowerNotExist) }
        return graph.uniquePaths.size.toLong()
    }

    fun part2(input: List<String>): Long {
        val graph = TheGraph(input).also { it.startTraversing(noTwiceLower) }
        return graph.uniquePaths.size.toLong()
    }

    part1(testInput).also {
        println("TEST part1: $it")
        check(it == 10L)
    }
    println("part1: ${part1(input)}")

    part2(testInput).also {
        println("TEST part2: $it")
        check(it == 36L)
    }
    println("part2: ${part2(input)}")
}


data class Rule(val pair: Pair<String, String>) {
    private val node1 = pair.first
    private val node2 = pair.second

    fun coversNode(node: String) = node1 == node || node2 == node
    fun toTarget(from: String): String {
        check(coversNode(from))
        return if (node1 == from) node2 else node1
    }
}

data class TheGraph(val input: List<String>) {
    val uniquePaths: MutableSet<List<String>> = mutableSetOf()
    private val rules = input.map { Rule(it.split("-").let { arr -> arr[0] to arr[1] }) }

    fun startTraversing(appendAllowed: (List<String>, String) -> Boolean) {
        traverseFrom("start", mutableListOf("start"), appendAllowed)
    }

    private fun traverseFrom(
        from: String,
        path: List<String>,
        appendAllowed: (List<String>, String) -> Boolean
    ) {
        rules.filter { it.coversNode(from) }.forEach foreach@{ rule ->
            val target = rule.toTarget(from)
            if (target == "start") return@foreach
            if (appendAllowed(path, target)) {
                val myPath = path + target
                if ("end" == target) {
                    uniquePaths.add(myPath).also { return@foreach }
                }
                traverseFrom(target, myPath, appendAllowed)
            }
        }
    }
}
