typealias Fish = Int
typealias FishGeneration = Map<Fish, Long>
typealias FishGenerationDelta = FishGeneration

/**
 * Converts [listOfFish] into a FishGeneration.
 */
fun fishGenerationOf(listOfFish: List<Fish>): FishGeneration {
    return listOfFish.groupingBy { it }.eachCount().mapValues { (_, count) -> count.toLong() }
}

fun main() {
    val DAY = 6

    /**
     * Returns what you get after evolving [fish] once.
     */
    fun evolveFish(fish: Fish): FishGenerationDelta {
        return when (fish) {
            0 -> mapOf(6 to 1L, 8 to 1L) // a fish of age 0 becomes one fish of age 6 and one fish of age 8
            else -> mapOf(fish - 1 to 1L) // a fish of age x becomes one fish of age x - 1
        }
    }

    /**
     * Scales up evolveFish to evolve [count] fish of the same kind.
     */
    fun evolveFishWithCount(fish: Fish, count: Long): FishGenerationDelta {
        return evolveFish(fish).mapValues { (_, thisFishCount) -> thisFishCount * count }
    }

    /**
     * Given a generation of fish, applies evolution rules to produce a new generation.
     */
    fun evolveFishGeneration(generation: FishGeneration): FishGeneration {
        val previousGeneration = generation.withDefault { 0L }

        val deltas = previousGeneration.map { (fish, count) -> evolveFishWithCount(fish, count) }

        // Merge all the fish generation deltas together to compute the new generation.
        val newGeneration = deltas.reduce(::mergeSummingConflictingValues)

        return newGeneration
    }

    /**
     * Evolves a generation of fish [epochs] times.
     */
    fun repeatEvolution(initialGeneration: FishGeneration, epochs: Int): FishGeneration {
        var generation = initialGeneration

        repeat(epochs) {
            generation = evolveFishGeneration(generation)
        }

        return generation
    }

    /**
     * Counts the total number of fish after evolving [generation] [epochs] times.
     */
    fun countFishAfterManyEvolutions(generation: FishGeneration, epochs: Int): Long {
        return repeatEvolution(generation, epochs).values.sum()
    }

    fun part1(input: List<String>): Long {
        val fish: List<Fish> = parseLineOfInts(input.first())
        val generation = fishGenerationOf(fish)

        return countFishAfterManyEvolutions(generation, 80)
    }

    fun part2(input: List<String>): Long {
        val fish: List<Fish> = parseLineOfInts(input.first())
        val generation = fishGenerationOf(fish)

        return countFishAfterManyEvolutions(generation, 256)
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("$DAY")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}


fun mainPrevious() {
    fun evolveFishV1(fish: Fish): List<Fish> {
        return when (fish) {
            0 -> listOf(6, 8)
            else -> listOf(fish - 1)
        }
    }

    fun part1V1(input: List<String>): Long {
        var fish: List<Fish> = input.first().split(",").map { it.toInt() }

        repeat(80) {
            // Ideally there would be way of doing this where fish is not mutated
            // Something like this:
            // return fish.flatMap(::evolveFish).flatMap(::evolveFish)/* and so on */.count()

            fish = fish.flatMap(::evolveFishV1)
        }

        return fish.count().toLong()
    }

    // The above naive approach is not optimised enough to work for simulations with a more demanding
    // number of epochs. Instead of keeping all the fish in a list, evolving them one at a time,
    // we batch fish of the same age together and evolve the entire generation at once. This performs
    // much better.

    /**
     * Given a generation of fish, applies evolution rules to return a new generation, describing
     * the initial generation one epoch later.
     */
    fun evolveFishGeneration(generation: FishGeneration): FishGeneration {
        val previousGeneration = generation.withDefault { 0L }

        // Ideally we'd break this up into a function that maps a fish of age X to a map of new ages of fish
        // like how we have evolveFish. Then, we'd fold that function over previousGeneration.
        val newGeneration = mapOf(
            8 to previousGeneration.getValue(0),
            7 to previousGeneration.getValue(8),
            6 to previousGeneration.getValue(7) + previousGeneration.getValue(0),
            5 to previousGeneration.getValue(6),
            4 to previousGeneration.getValue(5),
            3 to previousGeneration.getValue(4),
            2 to previousGeneration.getValue(3),
            1 to previousGeneration.getValue(2),
            0 to previousGeneration.getValue(1),
        )

        return newGeneration
    }

}

