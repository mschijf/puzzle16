package ms.puzzle16

/*
 *  +-----+-----+-----+-----+
 *  |  0  |  1  |  2  |  3  |
 *  +-----+-----+-----+-----+
 *  |  4  |  5  |  6  |  7  |
 *  +-----+-----+-----+-----+
 *  |  8  |  9  |  10 |  11 |
 *  +-----+-----+-----+-----+
 *  | 12  | 13  |  14 |  15 |
 *  +-----+-----+-----+-----+
 *
 *  +-----+-----+-----+-----+
 *  |  0  |  1  |  2  |  3  |
 *  +-----+-----+-----+-----+
 *  |  4  |  5  |  6  |  7  |
 *  +-----+-----+-----+-----+
 *  |  8  |  9  |  10 |  11 |
 *  +-----+-----+-----+-----+
 */

//    private val squareTiles = (0 until height).flatMap { row -> (0 until width).map { col -> row*width + col }}.toMutableList()
private const val SPACE_VALUE = 0
private const val NULL_VALUE = 30

data class SlidingTilePuzzle(
    private val squareTiles: List<Int>,
    private val width: Int,
    private val height: Int) {

    companion object {
        fun initialPuzzle16(): SlidingTilePuzzle {
            return SlidingTilePuzzle(listOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15), 4, 4)
        }

        fun cleanedPuzzle16(keepTiles: Set<Int>): SlidingTilePuzzle {
            return SlidingTilePuzzle ((0..15).map { if (it in keepTiles + 0) it else NULL_VALUE }.toList(), 4, 4)
        }
    }


    fun test() {
        println(this)
    }

    fun toList(): List<Int> {
        return squareTiles
    }

    fun toDensedList(): List<Int> {
        return squareTiles
            .mapIndexed { index, value -> Pair(index, value) }
            .filter { it.second != NULL_VALUE }
            .sortedBy { it.second }
            .map { it.first }
    }



    private fun powerIndex(digits: List<Int>, base: Int): Int {
        var result = 0
        for (digit in digits) {
            result = result * base + digit
        }
        return result
    }

    fun successors(): List<Pair<Int, SlidingTilePuzzle>> {
        val spaceIndex = squareTiles.indexOf(SPACE_VALUE)
        return createMovesTo(spaceIndex)
            .map { moveTo -> Pair(squareTiles[moveTo], doMoveTo(spaceIndex, moveTo)) }
    }

    fun doMoveTo(spaceIndex: Int, moveTo: Int): SlidingTilePuzzle {
        val squareTilesArray = squareTiles.toIntArray()
        squareTilesArray[spaceIndex] = squareTiles[moveTo]
        squareTilesArray[moveTo] = SPACE_VALUE
        return SlidingTilePuzzle(squareTilesArray.toList(), width, height)
    }

    override fun toString(): String {
        return squareTiles
            .toList()
            .map { if (it == SPACE_VALUE) ' ' else (it+65).toChar() }
            .chunked(width)
            .joinToString(",")
    }

    private fun createMovesTo(from: Int): List<Int> {
        val result = mutableListOf<Int>()
        val row = from / width
        val col = from % width
        if (row != 0) {
            result.add((row-1)*width + col)
        }
        if (col != 0) {
            result.add(row*width + col - 1)
        }
        if (col != width-1) {
            result.add(row*width + col + 1)
        }
        if (row != height-1) {
            result.add((row+1)*width + col)
        }
        return result
    }
}