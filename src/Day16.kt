// https://adventofcode.com/2021/day/16
fun main() {
    val today = "Day16"
    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Long {
        return Packet(input).also { it.parse(maxNo = 1) }.sumVer.toLong()
    }

    fun part2(input: List<String>): Long {
        return Packet(input).parse(maxNo = 1)
    }

    chkTestInput(part1(testInput), 31L, "Part 1")
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput), 54L, "Part 2")
    println("[Part2]: ${part2(input)}")
}

data class Packet(val input: List<String>) {
    private val bits = input[0].map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
    private var cursor = 0
    var sumVer = 0

    private fun nextAsInt(take: Int) = bits.substring(cursor, cursor + take).toInt(2).also { cursor += take }

    fun parse(maxNo: Int = Int.MAX_VALUE, maxLen: Int = Int.MAX_VALUE, type: Int = -1): Long {
        val values = mutableListOf<Long>()
        var noOfPacket = 0
        val mark = cursor
        while (++noOfPacket <= maxNo && cursor - mark < maxLen) {
            sumVer += nextAsInt(3)
            val myType = nextAsInt(3)
            if (myType == 4) {
                values += parseLiteralNumber()
            } else {
                when (nextAsInt(1)) { //indicator
                    0 -> values += parse(maxLen = nextAsInt(15), type = myType) //by len
                    1 -> values += parse(maxNo = nextAsInt(11), type = myType) // by num
                }
            }
        }

        return when (type) {
            0 -> values.sum()
            1 -> values.fold(1L) { acc, i -> acc * i }
            2 -> values.minOrNull()!!
            3 -> values.maxOrNull()!!
            5 -> (if (values[0] > values[1]) 1L else 0L).also { require(values.size == 2) }
            6 -> (if (values[0] < values[1]) 1L else 0L).also { require(values.size == 2) }
            7 -> (if (values[0] == values[1]) 1L else 0L).also { require(values.size == 2) }
// ----
            -1 -> values[0].also { require(values.size == 1) }
            else -> throw IllegalStateException("error: type=$type")
        }
    }

    private fun parseLiteralNumber(): Long {
        var tmp = 0L
        while (nextAsInt(1) == 1) {
            tmp = (tmp shl 4) + nextAsInt(4)
        }
        return (tmp shl 4) + nextAsInt(4)
    }
}