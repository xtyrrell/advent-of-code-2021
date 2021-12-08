import java.lang.RuntimeException

fun main() {
    val DAY = 8

    val easyDigitsSegmentCounts = listOf(2, 4, 3, 7)

    fun countEasyDigits(entries: List<Pair<List<String>, List<String>>>): Int {
        return entries.sumOf { it.second.count { it.length in easyDigitsSegmentCounts } }
    }

    fun sharedSegments(word1: String?, word2: String?): Int {
        if (word1 == null || word2 == null) throw IllegalArgumentException("Cannot check shared segments if a word is null.")

        return (word1.toSet() intersect word2.toSet()).size
    }

    fun String.sorted(): String = this.toCharArray().sorted().joinToString("")

    fun solveOneDisplay(entry: Pair<List<String>, List<String>>): Int {
        val wordsToDigits = mutableMapOf<String, Int>()
        val digitsToWords = mutableMapOf<Int, String>()

        val words = entry.first.sortedBy { it.length }.map { it.sorted() }.toMutableList()

        fun List<String>.excludingIdentified(): List<String> {
            return this.filterNot { it in wordsToDigits }
        }

        fun identify(word: String, digit: Int) {
            wordsToDigits[word] = digit
            digitsToWords[digit] = word
            words.remove(word)
        }

        /*
        step 1: identity 1, 4, 7, and 8
          by their unique lengths
        */
        identify(words.single { it.length == 2 }, 1)
        identify(words.single { it.length == 3 }, 7)
        identify(words.single { it.length == 4 }, 4)
        identify(words.single { it.length == 7 }, 8)

        /*
        step 2: identify 3
          from all 5-length words, 3 is the word which shares TWO segments with 1
        */
        val wordsWithLength5 = words.filter { it.length == 5 }
        identify(wordsWithLength5.single { sharedSegments(it, digitsToWords[1]) == 2 }, 3)

        /*
        step 3: identify 6, 9 and 0
          from all 6-length words, 6 is the word which shares ONE segment with 1
          from all 6-length words excluding 6, 9 is the word which shares FOUR segments with 4
          the 6-length word that is not 6 or 9 is 0
        */
        val wordsWithLength6 = words.filter { it.length == 6 }
        identify(wordsWithLength6.single { sharedSegments(it, digitsToWords[1]) == 1 }, 6)
        identify(wordsWithLength6.excludingIdentified().single { sharedSegments(it, digitsToWords[4]) == 4 }, 9)
        identify(wordsWithLength6.excludingIdentified().single(), 0)

        /*
        step 4:
          from all 5-length words excluding 3, 2 is the word that shares FOUR segments with 9
          the remaining word is 5
         */
        identify(wordsWithLength5.excludingIdentified().single { sharedSegments(it, digitsToWords[9]) == 4 }, 2)
        identify(wordsWithLength5.excludingIdentified().single(), 5)

        println("wordsToDigits")
        println(wordsToDigits)

        return entry.second.map { wordsToDigits[it.sorted()] ?: throw RuntimeException("Word $it was not solved.") }.joinToString("").toInt()
    }

    fun solve(entries: List<Pair<List<String>, List<String>>>): Int {
        return entries.map(::solveOneDisplay).sum()
    }

    fun part1(input: List<String>): Int {
        val entries = input.map { it.split(" | ").map { it.split(" ") } }.map { it.toPair() }

        return countEasyDigits(entries)
    }

    fun part2(input: List<String>): Int {
        val entries = input.map { it.split(" | ").map { it.split(" ") } }.map { it.toPair() }

        return solve(entries)
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 26)
    val testInput2 = readInput("${DAY}_test")
    check(part2(testInput2) == 61229)

    val input = readInput("$DAY")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}

