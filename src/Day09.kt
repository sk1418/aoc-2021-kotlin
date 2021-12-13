// https://adventofcode.com/2021/day/9
fun main() {
    val today = "Day09"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int {
        return HeightMap(input).findLowPoints().sumOf { it + 1 }
    }

    fun part2(input: List<String>): Int {
        return HeightMap(input).findBasins().map { it.size }.sortedByDescending { it }.take(3).reduce { a, b -> a * b }
    }

    chkTestInput(part1(testInput), 15, "Part 1")
    println("[Part 1]: ${part1(input)}")

    chkTestInput(part2(testInput), 1134, "Part 2")
    println("[Part 2]: ${part2(input)}")
}

data class HeightMap(val input: List<String>) {
    private val matrix: List<List<Int>> = input.map { it.toList().map { c -> c.digitToInt() } }
    private val maxI = matrix.size - 1
    private val maxJ = matrix[0].size - 1

    fun findLowPoints(): List<Int> = findLowPositions().map { p -> matrix[p.first][p.second] }

    //extension functions
    private fun List<List<Int>>.byPair(p: Pair<Int, Int>) = this[p.first][p.second]

    private fun Pair<Int, Int>.left() = Pair(first, second - 1)
    private fun Pair<Int, Int>.right() = Pair(first, second + 1)
    private fun Pair<Int, Int>.up() = Pair(first - 1, second)
    private fun Pair<Int, Int>.down() = Pair(first + 1, second)
    private fun Pair<Int, Int>.outOfBound() = first < 0 || first > maxI || second < 0 || second > maxJ
    //-------------------

    private fun findLowPositions(): List<Pair<Int, Int>> {
        val lowPos = mutableListOf<Pair<Int, Int>>()
        for (i in 0..maxI) {
            for (j in 0..maxJ) {
                if (isLow(Pair(i, j))) {
                    lowPos += Pair(i, j)
                }
            }
        }
        return lowPos
    }

    private fun isLow(pos: Pair<Int, Int>) = matrix[pos.first][pos.second].let { value ->
        booleanArrayOf(
            pos.left().let { it.outOfBound() || matrix.byPair(it) > value },
            pos.right().let { it.outOfBound() || matrix.byPair(it) > value },
            pos.up().let { it.outOfBound() || matrix.byPair(it) > value },
            pos.down().let { it.outOfBound() || matrix.byPair(it) > value },
        ).all { it }
    }

    fun findBasins(): MutableList<List<Pair<Int, Int>>> {
        val basins = mutableListOf<List<Pair<Int, Int>>>()
        findLowPositions().forEach {
            val bSet: MutableSet<Pair<Int, Int>> = mutableSetOf()
            putPointInBasin(it, bSet)
            basins.add(bSet.toList())
        }
        return basins
    }

    private fun putPointInBasin(pos: Pair<Int, Int>, bSet: MutableSet<Pair<Int, Int>>) {
        bSet.add(pos)
        pos.left().takeIf { canBePutInBasin(it, bSet) }?.apply { putPointInBasin(this, bSet) }
        pos.right().takeIf { canBePutInBasin(it, bSet) }?.apply { putPointInBasin(this, bSet) }
        pos.up().takeIf { canBePutInBasin(it, bSet) }?.apply { putPointInBasin(this, bSet) }
        pos.down().takeIf { canBePutInBasin(it, bSet) }?.apply { putPointInBasin(this, bSet) }
    }

    private fun canBePutInBasin(point: Pair<Int, Int>, bSet: MutableSet<Pair<Int, Int>>) =
        !point.outOfBound() && matrix.byPair(point) != 9 && !bSet.contains(point)
}