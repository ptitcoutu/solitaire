package solitaire

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class GameStats(
    val minNumberOfBalls: MutableState<Int> = mutableStateOf(37),
    val moves: MutableState<List<Move>> = mutableStateOf(emptyList()),
    val executedMoves: MutableState<List<Move>> = mutableStateOf(emptyList())
)