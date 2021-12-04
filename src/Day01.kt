fun main() {
    val DAY = 1

    fun part1(input: List<String>): Int {
        val depths = input.map { it.toInt() }
        val numberOfDepthIncreases = depths.zipWithNext().count { (x: Int, y: Int) -> y > x }

        return numberOfDepthIncreases
    }

    fun part2(input: List<String>): Int {
        val depths = input.map { it.toInt() }
        val windows = depths.windowed(3, transform = { window -> window.sum() })
        val numberOfWindowDepthIncreases = windows.zipWithNext().count { (x: Int, y: Int) -> y > x }

        return numberOfWindowDepthIncreases
    }

    // Test if implementation meets criteria from the description, like:
//    val testInput = readInput("${DAY}_test")
//    check(part1(testInput) == 1)

    val input = readInput("$DAY")
    println(part1(input))
    println(part2(input))
}
