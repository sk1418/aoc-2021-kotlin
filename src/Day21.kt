// https://adventofcode.com/2021/day/21
fun main() {
    val today = "Day21"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun toPlayers(input: List<String>) = input.mapIndexed { i, s -> Player(i + 1, s.split(Regex(": "))[1].toInt()) }
    fun part1(input: List<String>): Long {
        return DiceGame(toPlayers(input)).apply { play() }.also { println(it.players) }
            .let { g ->
                println(g.dice.rollCounts)
                g.dice.rollCounts * g.players.first { it.win.not() }.score
            }.toLong()
    }

    fun part2(input: List<String>): Long {
        //don't understand the problem... :-(
        return 0
    }

    chkTestInput(part1(testInput), 739785L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 0L, Part2)
    println("[Part2]: ${part2(input)}")
}

data class Player(val id: Int, var pos: Int, var score: Int = 0) {
    var win = false
}

data class DiceGame(val players: List<Player>) {
    val dice = DeterministicDice()
    private val goal = 1000

    fun play() {
        while (true) {
            players.onEach { p ->
                oneMove(p)
                if (p.score >= goal) {
                    p.win = true
                    return
                }
            }
        }
    }

    private fun oneMove(player: Player) = dice.sum3RollsBy10().also { m ->
        with(player) {
            pos = (m + pos - 1) % 10 +1
            score += pos
            println("Player${player.id} Step:$m Pos: $pos Score:$score")
        }
    }
}

data class DeterministicDice(var rollCounts: Int = 0) {
    private var next = 1
    private fun oneThrow() = (next - 1) % 100 +1.also{next++;rollCounts++}
    fun sum3RollsBy10() = (1..3).sumOf { oneThrow() } % 10
}
