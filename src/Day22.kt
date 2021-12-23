import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2021/day/22
fun main() {
    val today = "Day22"

    val input = readInput(today)
    val testInput = readTestInput(today)
    val testInput2 = readTestInput("$today-2")

    fun toRange(dotdot: String) = dotdot.split(Regex("[.][.]")).let { it[0].toInt()..it[1].toInt() }
    fun part1(input: List<String>): Long {
        val steps = input.map { str ->
            str.split(Regex("[ ,][xyz]=")).let { parts ->
                RebootStep(parts[0] == "on", toRange(parts[1]), toRange(parts[2]), toRange(parts[3])
                )
            }
        }
        return Rebooter(steps).reboots()
    }

    fun toCoordRange(dotdot: String) = dotdot.split(Regex("[.][.]")).let { CoordRange(it[0].toInt(), it[1].toInt()) }
    fun part2(input: List<String>): Long {
        val step2s = input.map { str ->
            str.split(Regex("[ ,][xyz]=")).let { parts ->
                RebootStep2(parts[0] == "on",
                    Area(toCoordRange(parts[1]), toCoordRange(parts[2]), toCoordRange(parts[3])))
            }
        }
        return RebooterPart2(step2s).dpCountON()
    }

    chkTestInput(part1(testInput), 590784L, Part1)
    println("[Part1]: ${part1(input)}")

    chkTestInput(part2(testInput2), 2758514936282235L, Part2)
    println("[Part2]: ${part2(input)}")
}


data class RebootStep(val switch: Boolean, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)


data class Rebooter(val steps: List<RebootStep>) {
    //extension func,the stadndard intersect() is too slow, since it converts into Sets, travers all elements
    private fun IntRange.intersect(theOther: IntRange) = max(this.first, theOther.first)..min(this.last, theOther.last)
    data class Coord(val x: Int, val y: Int, val z: Int)

    private val effRange50 = -50..50
    private val statusMap = hashMapOf<Coord, Boolean>()

    fun reboots(): Long { //part1
        steps.forEach { step ->
            effRange50.intersect(step.xRange).forEach { x ->
                effRange50.intersect(step.yRange).forEach { y ->
                    effRange50.intersect(step.zRange).forEach { z ->
                        statusMap[Coord(x, y, z)] = step.switch
                    }
                }
            }
        }
        return statusMap.values.count { it }.toLong()
    }
}
// **************************************************
// part 2
// **************************************************
data class RebootStep2(val switch: Boolean, val area: Area)
data class CoordRange(val from: Int, val to: Int) { //for part2
    val isEmpty = to < from
    val size: Long = if (isEmpty) 0 else (to.toLong() - from + 1)
    fun intersect(that: CoordRange) = CoordRange(max(this.from, that.from), min(this.to, that.to))
}

data class Area(val xRange: CoordRange, val yRange: CoordRange, val zRange: CoordRange) { //for part2
    val isEmpty: Boolean = xRange.isEmpty || yRange.isEmpty || zRange.isEmpty
    val cubeNumber: Long = if (isEmpty) 0 else xRange.size * yRange.size * zRange.size
    fun intersect(that: Area) =
        Area(xRange.intersect(that.xRange), yRange.intersect(that.yRange), zRange.intersect(that.zRange))
}

data class RebooterPart2(val steps: List<RebootStep2>) {
    private val fullArea = Area(
        CoordRange(Int.MIN_VALUE, Int.MAX_VALUE),
        CoordRange(Int.MIN_VALUE, Int.MAX_VALUE),
        CoordRange(Int.MIN_VALUE, Int.MAX_VALUE),
    )

    fun dpCountON() = dpCountOnAfterStep(steps.lastIndex, fullArea)

    /**  dynamic programming to count:
     * intersection = intersect [Area] of this step and the effect area(init. FullArea)
     * outArea = outside the intersection [Area] above
     * preCntInter = the Count of pre-step on the intersection
     * if current step is "turn on" = count(outArea.cube) + count(intersection.cube)
     * if current step is "turn off" = count(outArea.cube)
     */
    private fun dpCountOnAfterStep(stepIdx: Int, area: Area): Long {
        val preIdx = stepIdx - 1
        if (preIdx < 0 || area.isEmpty) {
            return 0
        }

        val intersectArea = steps[stepIdx].area.intersect(area)
        val countInPreStep = dpCountOnAfterStep(preIdx, area)
        val countIntersectArea = dpCountOnAfterStep(preIdx, intersectArea)
        val countOutArea = countInPreStep - countIntersectArea

        return if (!steps[stepIdx].switch) countOutArea else intersectArea.cubeNumber + countOutArea
    }
}


