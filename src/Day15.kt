import java.util.*

// https://adventofcode.com/2021/day/15
fun main() {
    val today = "Day15"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        return GraphDay15(input).findThePath()
    }

    fun part2(input: List<String>): Long {
        return GraphDay15(input, 4).findThePath()
    }

    chkTestInput(part1(testInput), 40L, "Part 1")
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 315L, "Part 2")
    println("[Part2]: ${part2(input)}")
}

data class Node(
    val pos: Pair<Int, Int>, val weight: Int,
    var distance: Int = Int.MAX_VALUE, var visited: Boolean = false, var preNode: Node? = null,
)

data class GraphDay15(val input: List<String>, val extTimes: Int = 0) {
    private val finalInput = input.map { row ->
        row.toList().map { it.digitToInt() }.toMutableList().apply {
            repeat(extTimes) { t ->
                this += row.toList().map { e -> (e.digitToInt() + t + 1).let { if (it > 9) it - 9 else it } }
            }
        }.toList()
    }.let { l ->
        l.toMutableList().apply {
            repeat(extTimes) { t ->
                this += l.map { row -> row.map { e -> (e + t + 1).let { if (it > 9) it - 9 else it } } }
            }
        }
    }

    private val nodes = finalInput.flatMapIndexed { i, row ->
        row.mapIndexed { j, n -> Pair(i, j) to Node(i to j, n) }
    }.toMap()
    private val pQueue = PriorityQueue(compareBy(Node::distance))
    private val maxI = finalInput.size - 1
    private val maxJ = finalInput[0].size - 1
    private val endNode = nodes[maxI to maxJ]!!

    fun findThePath(): Long {
        nodes[0 to 0]?.apply {
            distance = 0
            pQueue += this
        }
        while (!endNode.visited) {
            pQueue.remove().apply {
                updateNodeWithPQueue(pos.first - 1, pos.second, distance, pos)
                updateNodeWithPQueue(pos.first + 1, pos.second, distance, pos)
                updateNodeWithPQueue(pos.first, pos.second - 1, distance, pos)
                updateNodeWithPQueue(pos.first, pos.second + 1, distance, pos)
                visited = true
            }
        }
        return endNode.distance.toLong()
    }

    private fun updateNodeWithPQueue(i: Int, j: Int, d: Int, fromNode: Pair<Int, Int>) {
        if (i in 0..maxI && j in 0..maxJ)
            nodes[i to j]?.apply {
                if (d + weight < distance) {
                    distance = d + weight
                    preNode = nodes[fromNode]!!
                    pQueue += this
                }
            }
    }

    // slower version below, works for the part1, but takes too long for the part2
    fun findThePath_Slow(): Long {
        nodes[0 to 0]!!.distance = 0
        while (!endNode.visited) {
            nodes.values.filter { !it.visited && it.distance < Int.MAX_VALUE }.minByOrNull { it.distance }?.apply {
                updateNode(pos.first - 1, pos.second, distance, pos)
                updateNode(pos.first + 1, pos.second, distance, pos)
                updateNode(pos.first, pos.second - 1, distance, pos)
                updateNode(pos.first, pos.second + 1, distance, pos)
                visited = true
            }
        }
        return endNode.distance.toLong()
    }

    private fun updateNode(i: Int, j: Int, d: Int, fromNode: Pair<Int, Int>) {
        if (i in 0..maxI && j in 0..maxJ)
            nodes[i to j]?.apply {
                if (d + weight < distance) {
                    distance = d + weight
                    preNode = nodes[fromNode]!!
                }
            }
    }
}