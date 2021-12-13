// https://adventofcode.com/2021/day/8
fun main() {
    val today = "Day08"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun inAndOut(line: String) = line.split(" [|] ".toRegex()).let {
        it[0].split(" ") to it[1].split(" ")
    }


    fun part1(input: List<String>): Int {
        return input.sumOf { inAndOut(it).second.count { o -> o.length in setOf(2, 4, 3, 7) } }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val (digits, output) = inAndOut(it)
            val digMap = DigitsMap(digits)
            output.joinToString("") { n -> digMap.decode(n) }.toInt()
        }
    }

    chkTestInput(part1(testInput), 26, "Part 1")
    println("[Part 1]: ${part1(input)}")

    chkTestInput(part2(testInput), 61229, "Part 2")
    println("[Part 2]: ${part2(input)}")
}

/**
```
0:      1:      2:      3:      4:
aaaa    ....    aaaa    aaaa    ....
b    c  .    c  .    c  .    c  b    c
b    c  .    c  .    c  .    c  b    c
....    ....    dddd    dddd    dddd
e    f  .    f  e    .  .    f  .    f
e    f  .    f  e    .  .    f  .    f
gggg    ....    gggg    gggg    ....

5:      6:      7:      8:      9:
aaaa    aaaa    aaaa    aaaa    aaaa
b    .  b    .  .    c  b    c  b    c
b    .  b    .  .    c  b    c  b    c
dddd    dddd    ....    dddd    dddd
.    f  e    f  .    f  e    f  .    f
.    f  e    f  .    f  e    f  .    f
gggg    gggg    ....    gggg    gggg
```
 */
data class DigitsMap(val digits: List<String>) {
    private val digMap: MutableMap<HashSet<Char>, String> = mutableMapOf()

    init {
        digits.forEach { d ->
            val set = d.toHashSet()
            when (set.size) {
                2 -> digMap[set] = "1"
                3 -> digMap[set] = "7"
                4 -> digMap[set] = "4"
                7 -> digMap[set] = "8"
            }
        }
        digits.filter { it.length in 5..6 }.forEach {
            val set = it.toHashSet()
            when (it.length) {
                5 -> when { // 2, 3, 5 -> len=5
                    set.containsAll(getSet("1")) -> digMap[set] = "3"
                    set.intersect(getSet("4")).size == 3 -> digMap[set] = "5"
                    else -> digMap[set] = "2"
                }
                6 -> when { //6, 9, 0 -> len=6
                    set.intersect(getSet("1")).size == 1 -> digMap[set] = "6"
                    set.containsAll(getSet("4")) -> digMap[set] = "9"
                    else -> digMap[set] = "0"
                }
            }
        }
    }

    private fun getSet(v: String) = digMap.filterValues { it == v }.keys.first()

    fun decode(input: String) =
        digMap[input.toHashSet()] ?: throw RuntimeException("not gonna happen: $input")
}

