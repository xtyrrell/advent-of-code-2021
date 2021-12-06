import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("inputs", "$name.txt").readLines()

/**
 * Given a line of comma-separated numbers, returns a list of ints
 */
fun parseLineOfInts(line: String) = line.split(",").map { it.toInt() }

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Merges two maps, summing values for keys that conflict.
 */
fun <K> mergeSummingConflictingValues(map1: Map<K, Long>, map2: Map<K, Long>): Map<K, Long> {
    return (map1.keys + map2.keys).associateWith { k ->
        (map1[k] ?: 0) + (map2[k] ?: 0)
    }
}

/**
 * Zips [this] with [other], padding with [fillValue] if one list is shorter than the other.
 */
fun <T> List<T>.zipLongest(other: List<T>, fillValue: T): List<Pair<T, T>> {
    val thisPadding = List(maxOf(other.size - this.size, 0)) { fillValue }
    val otherPadding = List(maxOf(this.size - other.size, 0)) { fillValue }

    return (this + thisPadding) zip (other + otherPadding)
}

/**
 * Converts a two-element List into a Pair.
 */
fun <T> List<T>.toPair(): Pair<T, T> {
    if (this.size != 2) {
        throw IllegalArgumentException("Cannot convert a List whose size is not 2 to a Pair.")
    }
    return Pair(this[0], this[1])
}

infix fun Int.upOrDownTo(other: Int): IntProgression = if (this < other) this..other else this downTo other
