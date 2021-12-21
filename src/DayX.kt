// https://adventofcode.com/2021/day/7
//fun main() {
//    val today = "DayX"
//
//    val input = readInput(today)
//    val testInput = readTestInput(today)
//
//    fun part1(input: List<String>): Long {
//        return 0
//    }
//
//    fun part2(input: List<String>): Long {
//        return 0
//    }
//
//    chkTestInput(part1(testInput), 0L, Part1)
//    println("[Part1]: ${part1(input)}")
//
//    chkTestInput(part2(testInput), 0L, Part2)
//    println("[Part2]: ${part2(input)}")
//}
import java.io.File
import kotlin.math.max
import kotlin.math.min

val input = readTestInput("Day21")
val p1s = input[0].removePrefix("Player 1 starting position: ").toInt()
val p2s = input[1].removePrefix("Player 2 starting position: ").toInt()

var dCount = 0

val spaceVals = List(10) { it + 1 }
var dieRolls = 0
val dice = generateSequence<Int> {
    if (dCount == 100) dCount = 0
    dCount++
    dieRolls++
    dCount
}.iterator()

enum class CurrentPlayer {
    ONE,
    TWO
}

fun part1() {

    println(spaceVals)
    var p1score = 0
    var p2score = 0

    var p1Pos = p1s - 1
    var p2Pos = p2s - 1
    var currPlayer = CurrentPlayer.ONE
    while (true) {
        if (max(p1score, p2score) >= 1000) break
        val seq = dice.asSequence().take(3).toList()
        val step = seq.sum()
//        println(seq)
        when (currPlayer) {
            CurrentPlayer.ONE -> {
                p1Pos += step
                p1Pos %= 10
                p1score += spaceVals[p1Pos]
                currPlayer = CurrentPlayer.TWO
                println("P1  Moved to space ${p1Pos + 1} for total score of $p1score")
            }
            CurrentPlayer.TWO -> {
                p2Pos += step
                p2Pos %= 10
                p2score += spaceVals[p2Pos]
                currPlayer = CurrentPlayer.ONE
                println("P2 Moved to space ${p2Pos + 1} for total score of $p2score")
            }
        }
    }
    println(p1score)
    println(p2score)
    println(dieRolls)
    println(min(p1score, p2score) * dieRolls)
}

fun main() {
    part1()
}
