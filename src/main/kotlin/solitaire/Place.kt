package solitaire

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Place(
    val row: Int,
    val col: Int,
    val valid: MutableState<Boolean> = mutableStateOf(true),
    val hasBall: MutableState<Boolean> = mutableStateOf(false)
) {
    fun initFromCharConf(charConf: Char): Place {
        when (charConf) {
            'o' -> {
                valid.value = true
                hasBall.value = false
            }
            'x' -> {
                valid.value = true
                hasBall.value = true
            }
            else -> {
                valid.value = false
                hasBall.value = false
            }
        }
        return this
    }

    fun neighbour(allPlaces: List<List<Place>>, direction: Direction): List<Place> = when (direction) {
        Direction.up -> {
            if (row > 0) listOf<Place>(allPlaces[row - 1][col]) else emptyList<Place>()
        }
        Direction.down -> {
            if (row < allPlaces.size - 1) listOf<Place>(allPlaces[row + 1][col]) else emptyList<Place>()
        }
        Direction.left -> {
            if (col > 0) listOf<Place>(allPlaces[row][col - 1]) else emptyList<Place>()
        }
        Direction.right -> {
            if (col < allPlaces[row].size - 1) listOf<Place>(allPlaces[row][col + 1]) else emptyList<Place>()
        }
    }


    private fun checkPossibleMovesInDirection(allPlaces: List<List<Place>>, direction: Direction, nbBalls: Int): List<Move> {
        val directNeighbour = neighbour(allPlaces, direction)
        if (directNeighbour.size == 1 && directNeighbour[0].let { it.hasBall.value && it.valid.value }) {
            val neighbourOfNeighbour = directNeighbour[0].neighbour(allPlaces, direction)
            if (neighbourOfNeighbour.size == 1 && neighbourOfNeighbour[0].let { !it.hasBall.value && it.valid.value }) {
                return listOf(Move(sourcePlace = this, targetPlace = neighbourOfNeighbour[0], direction = direction, nbBalls = nbBalls))
            }
        }
        return emptyList()
    }

    fun checkPossibleMoves(allPlaces: List<List<Place>>, nbBalls: Int): List<Move> {
        return if (this.hasBall.value)
            Direction.values().flatMap { checkPossibleMovesInDirection(allPlaces, it, nbBalls) }
        else
            emptyList()
    }
}