import kotlin.math.abs

fun main() {
    val DAY = 7

    /**
     * Returns `1 + 2 + ... + n`.
     */
    fun sumToN(n: Int): Int = n * (n + 1) / 2

    fun calculateFuelPart2(crabPosition: Int, numberOfCrabsMoving: Int, targetPosition: Int): Int {
        val stepsToMove = abs(crabPosition - targetPosition)

        return sumToN(stepsToMove) * numberOfCrabsMoving
    }

    fun calculateFuelPart1(crabPosition: Int, numberOfCrabsMoving: Int, targetPosition: Int): Int {
        return abs(crabPosition - targetPosition) * numberOfCrabsMoving
    }


    fun solve(crabsFrequencies: Map<Int, Int>, calculateFuel: (Int, Int, Int) -> Int): Long {
        // krab->freqeuncy pairs sorted by frequncy
//        val frequencies = crabs.groupingBy { it }.eachCount().entries.sortedBy { it.value }

        var lowestCost = Long.MAX_VALUE
        // brute-force: try each possible horizontal alignment candidate and compute scores
        for ((candidateCrabPosition, _) in crabsFrequencies) {
            // try aligning at [crab] position
            val thisCost = crabsFrequencies.map{ (crabPosition, frequency) -> calculateFuel(crabPosition, frequency, candidateCrabPosition) }.sum().toLong()
            if (thisCost < lowestCost) lowestCost = thisCost
        }

        return lowestCost
    }

    fun part1(input: List<String>): Long {
        val crabs: List<Int> = parseLineOfInts(input.first())
        val crabsFrequencies = buildFrequencyMap(crabs)

        return solve(crabsFrequencies, ::calculateFuelPart1)
    }

    fun part2(input: List<String>): Long {
        val crabs: List<Int> = parseLineOfInts(input.first())
        val crabsFrequencies = buildFrequencyMap(crabs)

        return solve(crabsFrequencies, ::calculateFuelPart2)
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 37L)
//    check(part2(testInput) == 26984457539L)

    val input = readInput("$DAY")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
