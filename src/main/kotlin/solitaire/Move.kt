package solitaire

import androidx.compose.runtime.MutableState

data class Move(val sourcePlace: Place, val targetPlace: Place, val direction: Direction, val nbBalls: Int) {
    fun execute(allPlaces: List<List<Place>>) {
        val directNeighbour = sourcePlace.neighbour(allPlaces, direction)
        directNeighbour[0].hasBall.value = false
        sourcePlace.hasBall.value = false
        targetPlace.hasBall.value = true
    }

    fun revert(allPlaces: List<List<Place>>) {
        val directNeighbour = sourcePlace.neighbour(allPlaces, direction)
        directNeighbour[0].hasBall.value = true
        sourcePlace.hasBall.value = true
        targetPlace.hasBall.value = false
    }
}