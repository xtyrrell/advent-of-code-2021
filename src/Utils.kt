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
