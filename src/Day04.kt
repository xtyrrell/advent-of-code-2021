fun main() {
    val DAY = 4

    class BoardPosition(val number: Int, var seen: Boolean)
    class BingoBoard(rowsOfNumbers: List<List<Int>>) {
        val rows = rowsOfNumbers.map { row -> row.map { BoardPosition(it, false) }}

        /**
         * Marks the BoardPosition with this number as seen.
         */
        fun draw(number: Int) {
            for (i in rows.indices) {
                for (j in 0 until rows[0].size) {
                    val boardNumber = rows[i][j]

                    if (number == boardNumber.number) {
                        rows[i][j].seen = true
                    }
                }
            }
        }

        /**
         * Returns `rows` transposed.
         */
        fun getCols(): List<List<BoardPosition>> {
            val cols = mutableListOf<MutableList<BoardPosition>>()

            for (colPos in rows[0].indices) {
                val col = mutableListOf<BoardPosition>()

                for (rowPos in rows.indices) {
                    col.add(rows[rowPos][colPos])
                }

                cols.add(col)
            }

            return cols
        }

        fun calculateScoreIfWon(): Int? {
            for (row in rows) {
                if (row.all { it.seen }) return calculateScore()
            }
            for (col in getCols()) {
                if (col.all { it.seen }) return calculateScore()
            }

            return null
        }

        fun hasWon(): Boolean {
            return calculateScoreIfWon() != null
        }

        private fun calculateScore(): Int {
            return rows.flatten().filterNot {it.seen}.sumOf { it.number }
        }
    }

    /**
     * Reads in board rows from input and constructs an array of boards, where each was
     * separated by an empty line in the input.
     */
    fun readBoards(boardLines: List<String>): List<BingoBoard> {
        val boards = mutableListOf<BingoBoard>()

        val rows = mutableListOf<List<Int>>()
        for (line in boardLines) {
            if (line.isEmpty()) {
                boards.add(BingoBoard(rows))
                rows.clear()
                continue
            }

            val row = line.split(Regex("(\\s)+")).filterNot { it.isEmpty() }.map {it.toInt()}
            rows.add(row)
        }

        return boards
    }

    /**
     * Returns the score of the first winning board multiplied by the winning draw.
     */
    fun part1(input: List<String>): Int {
        val draws: List<Int> = input.first().split(",").map {it.toInt()}
        val boards: List<BingoBoard> = readBoards(input.drop(2).plus(""))

        for (draw in draws) {
            boards.forEach { it.draw(draw) }

            val winningBoardScore = boards.firstOrNull { it.hasWon() }?.calculateScoreIfWon()
            if (winningBoardScore != null) {
                val finalScore = winningBoardScore * draw
                return finalScore
            }
        }

        return 0
    }

    /**
     * Returns the score of the last winning board multiplied by the winning draw.
     *
     * This function is quite imperative :(
     */
    fun part2(input: List<String>): Int {
        val draws: List<Int> = input.first().split(",").map {it.toInt()}
        val boards: MutableList<BingoBoard> = readBoards(input.drop(2).plus("")) as MutableList<BingoBoard>

        val scores = mutableListOf<Int>()
        for (draw in draws) {
            boards.forEach { it.draw(draw) }

            val winningBoards = boards.filter { it.hasWon() }

            for (winningBoard in winningBoards) {
                scores.add(winningBoard.calculateScoreIfWon() as Int * draw)
            }

            boards.removeAll(winningBoards)
        }

        return scores.last()
    }

    val testInput = readInput("${DAY}_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("$DAY")
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
}
