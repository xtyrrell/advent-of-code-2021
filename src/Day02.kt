import java.io.File

fun main() {
    val DAY = 2

    fun part1(input: List<String>): Int {
        throw NotImplementedError("Oops, I accidentally did not save my solution to part 1 :|")
    }

    fun part2(input: List<String>): Int {
        class Position(var depth: Int = 0, var x: Int = 0, var aim: Int = 0)

        /**
         * Given a position, returns a function that can be called many times to iteratively
         * set the position. I probably should have just made this a method in Position.
         */
        val updatePosition = { position: Position ->
            { line: String ->
                val (direction, magnitudeString) = line.split(" ")
                val magnitude = magnitudeString.toInt()

                when (direction) {
                    "down" -> position.aim += magnitude
                    "up" -> position.aim -= magnitude
                    "forward" -> {
                        position.x += magnitude
                        position.depth += magnitude * position.aim
                    }
                }
            }
        }

        val position = Position()

        // I could have / should have used reduce but this is not that bad and I was feeling lazy
        input.forEach(updatePosition(position))

        val answer = position.x * position.depth

        return answer
    }

    // Test if implementation meets criteria from the description, like:
//    val testInput = readInput("${DAY}_test")
//    check(part1(testInput) == 1)

    val input = readInput("$DAY")
    println(part1(input))
    println(part2(input))
}
