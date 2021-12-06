typealias Coords = Pair<Int, Int>
typealias Line = List<Coords>

fun main() {
    val DAY = 5

    fun coordsFromShorthand(shorthand: String): Pair<Coords, Coords> =
        shorthand.split(" -> ").map { it.split(",").map { it.toInt() }.toPair() }.toPair()

    fun generateLine(start: Coords, finish: Coords): Line {
        val (x1, y1) = start
        val (x2, y2) = finish

        // Zip (from the start coordinate's X point to the finish coordinate's X point) with similar for Y.
        // We are given that all lines are either horizontal, vertical, or perfectly diagonal.
        // Therefore, if these two progressions are not the same length, then it must be that x1 == x2
        // or y1 == y2 and the shorter progression is actually some_number upOrDownTo some_number.
        // In this case, we just repeat that number every time it is needed by the other progression.
        return (x1 upOrDownTo x2).toList().zipLongest((y1 upOrDownTo y2).toList(), if (x1 == x2) x1 else y1)
    }

    fun part1(input: List<String>): Int {
        val occupiedCoords = input.filterNot { it.isBlank() }.map(::coordsFromShorthand).filter { (start, finish) ->
            val (x1, y1) = start
            val (x2, y2) = finish

            (x1 == x2) || (y1 == y2)
        }.flatMap { (start, finish) ->
            generateLine(start, finish)
        }

        return occupiedCoords.groupingBy { it }.eachCount().values.count { it >= 2 }
    }

    fun part2(input: List<String>): Int {
        val occupiedCoords = input.filterNot { it.isBlank() }.map(::coordsFromShorthand).flatMap { (start, finish) ->
            generateLine(start, finish)
        }

        return occupiedCoords.groupingBy { it }.eachCount().values.count { it >= 2 }
    }

    val testInput = readInput("${DAY}_test")
    val part1TestAnswer = part1(testInput)
    println("part 1 test: ${part1TestAnswer}")
    check(part1TestAnswer == 5)
    check(part2(testInput) == 12)

    val input = readInput("$DAY")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
