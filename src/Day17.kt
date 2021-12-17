import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.sqrt

// https://adventofcode.com/2021/day/17
fun main() {
    val today = "Day17"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        /* First of all: The part1 has nothing to do with X.
        - Given the position yHighst, we have Vy[highest]=0. First get m from the input "y=m..n":
        - when S=Vx0,Vy0 (0 axis), if Vy0>0, we always have Vy0 == abs(Vy0') ( Vy0' is the Vy when it falls back to 0 axis)
        - If we want to reach the highest Y, the m+1 would be the max initial Vy0'. Therefore, Vy0'=m+1, and Vy0=abs(Vy0')
        - The Vy[highest]=0, so, sum the sequence 0 -> Vy0 is the answer. */
        val m = input[0].split(Regex("(: |, )"))[2].split(Regex("y=|[.][.]"))[1].toInt()
        return (m * (m + 1) / 2).toLong()
    }

    fun part2(input: List<String>): Long {
        val (rangeX, rangeY) = input[0].split(Regex("[^0-9-]+"))
            .let { it[1].toInt()..it[2].toInt() to it[3].toInt()..it[4].toInt() }
        // get min possible Vx0 (minVx0), formula:  (minVx0 + 1) * minVx0/2 = rangeX.first
        val minPossibleVx0 = ceil((sqrt((4 * 2 * (rangeX.first + 1)).toDouble()) - 1) / 2).toInt()

        var count = 0
        (minPossibleVx0..rangeX.last).forEach { vx0 ->
            (rangeY.first..abs(rangeY.first + 1)).forEach { vy0 ->  // to the positive Vy0 that can produce the highest Y (part1)
                var (vx, vy) = vx0 to vy0
                var (posX, posY) = 0 to 0
                while (posX <= rangeX.last && posY >= rangeY.first) {
                    posX += if (vx > 0) (vx)-- else 0
                    posY += (vy)--
                    if (posX in rangeX && posY in rangeY) {
                        count++
                        break
                    }
                }
            }
        }
        return count.toLong()
    }

    chkTestInput(part1(testInput), 45L, "Part 1")
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 112L, "Part 2")
    println("[Part2]: ${part2(input)}")
}
