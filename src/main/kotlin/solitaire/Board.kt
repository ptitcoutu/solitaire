package solitaire

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.math.abs

const val ONE_BALL: Int = 1

data class Board(
    val initGameConf: List<String> = listOf(
        listOf(
            "  xxx  ",
            " xxxxx ",
            "xxxxxxx",
            "xxxoxxx",
            "xxxxxxx",
            " xxxxx ",
            "  xxx  "
        ), listOf(
            "  xxx  ",
            "  xxx  ",
            "xxxxxxx",
            "xxxoxxx",
            "xxxxxxx",
            "  xxx  ",
            "  xxx  "
        )
    )[0],
    val places: List<List<Place>> = initGameConf.mapIndexed { row, rowConf ->
        rowConf.mapIndexed { col, placeConf ->
            Place(row, col).initFromCharConf(placeConf)
        }
    },
    val gameStats: GameStats = GameStats(),
    val solverRunning: MutableState<Boolean> = mutableStateOf(false)
) {
    fun reset() {
        initGameConf.forEachIndexed { row, rowConf ->
            rowConf.forEachIndexed { col, placeConf ->
                places[row][col].initFromCharConf(placeConf)
            }
        }
    }

    private fun brutForceSolve(nbBalls: Int, badBoardIds : MutableSet<Int>): Boolean {
        if (nbBalls < gameStats.minNumberOfBalls.value) {
            gameStats.minNumberOfBalls.value = nbBalls
        }
        if (nbBalls > 1) {
            val boardId = processBoardId()
            if (badBoardIds.contains(boardId)) {
                return false
            }
            val possibleMoves = places.flatMap { rowOfPlaces ->
                rowOfPlaces.flatMap { sourcePlace ->
                    sourcePlace.checkPossibleMoves(places, nbBalls)
                }
            }
            if (possibleMoves.isNotEmpty()) {
                val isSolution = possibleMoves.any {
                    it.execute(places)
                    //delay(10)
                    //yield()
                    val brutForceResult = brutForceSolve(nbBalls - 1, badBoardIds)
                    if (brutForceResult) gameStats.moves.value = listOf(it) + gameStats.moves.value
                    it.revert(places)
                    //delay(10)
                    //yield()
                    brutForceResult
                }
                if (isSolution) {
                    return true
                } else {
                    badBoardIds.add(boardId)
                    return false
                }
            }
            return false
        } else {
            return true//places[3][3].hasBall.value
        }
    }
    private data class Orientation(val startRow: Int, val startCol: Int, val iterateByColumn: Boolean)
    private fun realPlaceFromPlaceLocation(place: Place) = places[place.row][place.col]
    fun processBoardId(): Int {
        val orientations = listOf<Orientation>(
            Orientation(startRow = 0, startCol = 0, iterateByColumn = true),
            Orientation(startRow = 0, startCol = 6, iterateByColumn = true),
            Orientation(startRow = 6, startCol = 6, iterateByColumn = true),
            Orientation(startRow = 6, startCol = 0, iterateByColumn = true),
            Orientation(startRow = 0, startCol = 0, iterateByColumn = false),
            Orientation(startRow = 0, startCol = 6, iterateByColumn = false),
            Orientation(startRow = 6, startCol = 6, iterateByColumn = false),
            Orientation(startRow = 6, startCol = 0, iterateByColumn = false)
        )
        return orientations.minOf { orientation ->
                (0..6).flatMap { mainIndex ->
                    (0..6).map { secondIndex ->
                        val (col, row) = if (orientation.iterateByColumn)
                            Pair(mainIndex, secondIndex)
                        else Pair(secondIndex, mainIndex)
                       if (places[abs(row-orientation.startRow)][abs(col-orientation.startCol)].hasBall.value) 1 shl (mainIndex*7+secondIndex) else 0
                    }
                }.sumOf { it }
        }
    }
    fun solve() {
        solverRunning.value = true
        val nbBalls = places.sumOf { placesRow -> (placesRow.sumOf { if (it.hasBall.value) ONE_BALL else 0 }) }
        val boardToSolve = copy(
            places = places.map { row -> row.map { it.copy(hasBall = mutableStateOf(it.hasBall.value)) } },
            gameStats = gameStats.copy(
                moves = mutableStateOf(emptyList()),
                executedMoves = mutableStateOf(emptyList())
            )
        )
        val badBoardIds = mutableSetOf<Int>()
        boardToSolve.brutForceSolve(nbBalls, badBoardIds)
        gameStats.moves.value = boardToSolve.gameStats.moves.value.map {
            it.copy(
                sourcePlace = realPlaceFromPlaceLocation(it.sourcePlace),
                targetPlace = realPlaceFromPlaceLocation(it.targetPlace)
            )
        }
        gameStats.executedMoves.value = emptyList()
        solverRunning.value = false
    }
}