import kotlin.math.abs

fun main() {
    val input = readInput("Day05")
    val sea = Sea(1000)

    fun getTwoPoints(line: String) =
        line.split(" -> ".toRegex()).map {
            val t = it.split(",")
            t[0].toInt() to t[1].toInt()
        }

    fun part1(input: List<String>): Int {
        input.forEach {
            getTwoPoints(it).apply { sea.drawLineHorV(this[0], this[1]) }
        }
        return sea.calcResult()
    }

    fun part2(input: List<String>): Int {
        input.forEach {
            getTwoPoints(it).apply { sea.drawLineDiagonal(this[0], this[1]) }
        }
        return sea.calcResult()
    }

    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

data class Sea(val dimension: Int) {
    private val matrix: Array<IntArray> = Array(dimension) { IntArray(dimension) { 0 } }

    fun drawLineHorV(p1: Pair<Int, Int>, p2: Pair<Int, Int>) {
        if (p1.first == p2.first) {
            getRange(p1.second, p2.second).forEach {
                matrix[p1.first][it]++
            }
        } else if (p1.second == p2.second) {
            getRange(p1.first, p2.first).forEach {
                matrix[it][p1.second]++
            }
        }
    }

    fun drawLineDiagonal(p1: Pair<Int, Int>, p2: Pair<Int, Int>) {
        val plusN: (Int, Int) -> Int = { i, n -> i + n }
        val minusN: (Int, Int) -> Int = { i, n -> i - n }
        val (dx, dy) = Pair((p1.first - p2.first), (p1.second - p2.second))
        if (abs(dx) == abs(dy)) {
            val xFun = if (dx < 0) plusN else minusN
            val yFun = if (dy < 0) plusN else minusN
            (0..abs(dx)).forEach {
                matrix[xFun(p1.first, it)][yFun(p1.second, it)]++
            }
        }
    }

    private fun getRange(a: Int, b: Int) = if (a > b) b..a else a..b

    fun calcResult(): Int = matrix.sumOf { it.count { p -> p > 1 } }
}

