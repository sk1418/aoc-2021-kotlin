fun main() {
    val input = readInput("Day02")

    fun part1(input: List<String>): Int {
        var v = 0
        var h = 0
        input.forEach {
            val p = it.split(" ")
            when (p[0]) {
                "forward" -> h += p[1].toInt()
                "up" -> v -= p[1].toInt()
                "down" -> v += p[1].toInt()
            }
        }
        return v * h
    }

    fun part2(input: List<String>): Int {
        var v = 0
        var h = 0
        var a = 0
        input.forEach {
            val p = it.split(" ")
            when (p[0]) {
                "forward" -> {
                    h += p[1].toInt()
                    v += a * p[1].toInt()
                }
                "up" -> a -= p[1].toInt()
                "down" -> a += p[1].toInt()
            }
        }
        return v * h
    }

    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
