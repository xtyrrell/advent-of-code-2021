typealias Fish = Int
typealias FishGeneration = Map<Fish, Long>
typealias FishGenerationDelta = FishGeneration

/**
 * Converts [listOfFish] into a FishGeneration.
 */
fun fishGenerationOf(listOfFish: List<Fish>): FishGeneration =
    listOfFish.groupingBy { it }.eachCount().mapValues { (_, count) -> count.toLong() }

fun main() {
    val DAY = 6

    /**
     * Returns what you get after evolving [fish] once.
     */
    fun evolveFish(fish: Fish): FishGenerationDelta = when (fish) {
        0 -> mapOf(6 to 1L, 8 to 1L) // a fish of age 0 becomes one fish of age 6 and one fish of age 8
        else -> mapOf(fish - 1 to 1L) // a fish of age x becomes one fish of age x - 1
    }

    /**
     * Scales up evolveFish to evolve [count] fish of the same kind.
     */
    fun evolveFishWithCount(fish: Fish, count: Long): FishGenerationDelta =
        evolveFish(fish).mapValues { (_, thisFishCount) -> thisFishCount * count }

    /**
     * Given a generation of fish, applies evolution rules to produce a new generation.
     */
    fun evolveFishGeneration(generation: FishGeneration): FishGeneration {
        val previousGeneration = generation.withDefault { 0L }

        val deltas = previousGeneration.map { (fish, count) -> evolveFishWithCount(fish, count) }

        // Merge all the fish generation deltas together to compute the new generation.
        return deltas.reduce(::mergeSummingConflictingValues)
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
    fun countFishAfterManyEvolutions(generation: FishGeneration, epochs: Int): Long =
        repeatEvolution(generation, epochs).values.sum()

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
