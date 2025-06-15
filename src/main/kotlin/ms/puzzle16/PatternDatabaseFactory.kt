package ms.puzzle16

import java.util.Locale

class PatternDatabaseFactory {

    private val patternDB = mutableMapOf<List<Int>, Int>()

    fun preCalculate() {
        val startTime = System.currentTimeMillis()
        var max = 0
        var count = 0

//        val checkSet = setOf(1,2,4,5,8)
        val checkSet = setOf(3, 6, 7, 10, 11)
//        val checkSet = setOf(9, 12,13,14,15)

//        val checkSet = setOf(1,2,4,5,8,9)
        val start = SlidingTilePuzzle.cleanedPuzzle16(checkSet)

        val queue = ArrayDeque<SlidingTilePuzzle>()
        queue.add(start)
        patternDB[start.toList()] = 0
        var inPattern = 1
        while (queue.isNotEmpty()) {
            if (count % 100_000 == 0) {
                val timePassed = System.currentTimeMillis() - startTime
                println()
                print("" +
                        "nodes examined: ${String.format(Locale.GERMANY, " %,d",count)} " +
                        " in pattern: ${String.format(Locale.GERMANY, " %,d",inPattern)}" +
                        " in queue: ${String.format(Locale.GERMANY, " %,d",queue.size)}" +
                        " maxMoves: $max" +
                        " (%d.%03d sec)".format(timePassed / 1000, timePassed % 1000) +
                        " ${String.format(Locale.GERMANY, " %,d",(1000.0*count*18.0 / timePassed).toInt())} nodes/sec" +
                        ""
                )
            }

            val current = queue.removeFirst()
            val movesDone = patternDB[current.toList()]!!
            max = maxOf(max, movesDone)
            count++

            current
                .successors()
                .forEach { (movedTile, next) ->
                    val ll = next.toList()
                    if (ll !in patternDB) {
                        if (movedTile in checkSet) {
                            patternDB[ll] = movesDone + 1
                        } else {
                            patternDB[ll] = movesDone
                        }
                        inPattern++
                        queue.add(next)
                    }
                }
        }
        val timePassed = System.currentTimeMillis() - startTime
        println()
        print("" +
                "nodes examined: ${String.format(Locale.GERMANY, " %,d",count)} " +
                " in pattern: ${String.format(Locale.GERMANY, " %,d",inPattern)}" +
                " in queue: ${String.format(Locale.GERMANY, " %,d",queue.size)}" +
                " maxMoves: $max" +
                " (%d.%03d sec)".format(timePassed / 1000, timePassed % 1000) +
                " ${String.format(Locale.GERMANY, " %,d",(1000.0*count*18.0 / timePassed).toInt())} nodes/sec" +
                ""
        )
        println()
        println()
        println("All done in %d.%03d sec".format(timePassed / 1000, timePassed % 1000))
        println("max moves: $max")
        println("Stored in pattern: $inPattern")
        println("start printing errors")
        max = patternDB.values.max()
        println("all printed")
        println("max moves (recalculated): $max")
        val summed = patternDB
            .map { it.key.toMutableList().cleanSpaceTile() to it.value  }
            .groupBy { it.first }
            .mapValues { entry -> entry.value.map { it.second }.distinct().count()    }
        println("summed.count: ${summed.size}")
        println("summed.min: ${summed.values.min()}")
        println("summed.max: ${summed.values.max()}")

    }


    private fun MutableList<Int>.cleanSpaceTile(): List<Int> {
        val idx = this.indexOf(0)
        this[idx] = 30
        return this
    }
}