import kotlin.math.ln

fun main() {
    val input = readInput("Day03")

    fun part1(input: List<String>): Int {
        val colSum = IntArray(input[0].length) { 0 }
        input.forEach {
            for (idx in it.indices) {
                colSum[idx] += it[idx].digitToInt()
            }
        }
        val n1 = colSum.joinToString("") { if (it * 2 > input.size) "1" else "0" }.toInt(2)
        val n2 = invertBits(n1)
        return n1 * n2
    }

    fun part2(input: List<String>): Int {
        val theList = input.toMutableList()
        val n1 = getTheNumber(theList) { sum, len -> if (sum * 2 >= len) '1' else '0' }
        val n2 = getTheNumber(theList) { sum, len -> if (sum * 2 >= len) '0' else '1' }
        return n1 * n2
    }

    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

fun getTheNumber(input: List<String>, oneOrZero: (sum: Int, len: Int) -> Char): Int {
    var myList = input
    for (idx in 0 until input[0].length) {
        val sum = myList.sumOf { numList -> numList[idx].digitToInt() }
        val f = oneOrZero(sum, myList.size)
        myList = myList.filter { numList -> numList[idx] == f }.toMutableList()
        if (myList.size == 1)
            break
    }
    return myList[0].toInt(2)
}


fun invertBits(n: Int): Int {
    var num = n
    val bits = (ln(num.toDouble()) / ln(2.0)).toInt() + 1
    (0 until bits).forEach{ num = num xor (1 shl it) }
    return num
}