typealias Binary = MutableList<Int>

fun main() {
    val DAY = 3

    fun part1(input: List<String>): Int {
        val updateBinaryCounts = { oneCounts: Binary ->
            { line: String ->
                val bits = line.map { it.toString().toInt() }

                for ((i, bit) in bits.withIndex()) {
                    oneCounts[i] += bit
                }
            }
        }

        // This is a bit gross -- I cleaned it up slightly for part2
        val oneCounts: Binary = mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0)

        input.forEach(updateBinaryCounts(oneCounts))

        val gamma = oneCounts.map { if (it > 500) 1 else 0 }.joinToString("")
        val epsilon = oneCounts.map { if (it < 500) 1 else 0 }.joinToString("")

        val g = gamma.toInt(2)
        val e = epsilon.toInt(2)

        println("g $g e $e")

        val answer = g * e

        return answer
    }

    fun part2(input: List<String>): Int {
        // Haha, the `leastCommon` parameter that inverts the behaviour is a bit cheeky, probably
        // wouldn't do this if it wasn't late at night lol
        fun mostCommonBitsProfile(bitstrings: List<String>, leastCommon: Boolean = false): String {
            val bitstringLength = bitstrings.first().length

            var currentBitProfile = ""
            for (i in 0 until bitstringLength) {
                val filteredBitstrings = bitstrings.filter { it.startsWith(currentBitProfile)}
                val filteredSize = filteredBitstrings.size

                if (filteredSize == 1) return filteredBitstrings.first()

                val ones = filteredBitstrings.map { it[i].toString().toInt() }.count { it == 1}
                val zeroes = filteredSize - ones

                val mostCommonBit = if (ones >= zeroes) 1 else 0
                val leastCommonBit = if (ones >= zeroes) 0 else 1

                val selectedBit = if (!leastCommon) mostCommonBit else leastCommonBit
                currentBitProfile += selectedBit.toString()
            }

            return currentBitProfile
        }

        var mostCommonBitsProfile = mostCommonBitsProfile(input)
        var leastCommonBitsProfile = mostCommonBitsProfile(input, true)

        val oxygenGeneratorRating = mostCommonBitsProfile.toInt(2)
        val co2ScrubberRating = leastCommonBitsProfile.toInt(2)

        val answer = oxygenGeneratorRating * co2ScrubberRating

        return answer
    }

    // Test if implementation meets criteria from the description, like:
    val testInput = readInput("${DAY}_test")
//    check(part1(testInput) == 198)

    val input = readInput("$DAY")
    println(part1(input))
    println(part2(input))
}
