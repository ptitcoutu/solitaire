// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import solitaire.Board
import solitaire.Place

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Solitaire"
    ) {
        MaterialTheme {
            Game()
        }
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
@Preview
fun Game() {
    val board = remember { Board() }
    return Column {
        (0..6).map { i ->
            Row {
                (0..6).map { j ->
                    Column {
                        val place = board.places[i][j]
                        if (place.valid.value) {
                            PlaceView(place, board.places)
                        } else {
                            Button(enabled = false, onClick = {}) { Text("   ") }
                        }

                    }
                }
            }
        }
        Row {
            Button(onClick = {
                board.reset()
            }) {
                Text("Réinitialiser")
            }
            Button(enabled = !board.solverRunning.value,onClick = {
                Thread {
                    board.solve()
                }.start()
            }) {
                Text("Résoudre")
            }
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(text = "    Min number of balls : ${board.gameStats.minNumberOfBalls.value}")
                if (board.solverRunning.value) {
                    CircularProgressIndicator()
                }
                Row {
                    Column {
                        board.gameStats.moves.value.mapIndexed { index, move ->
                            Button(onClick = {
                                move.execute(board.places)
                                board.gameStats.moves.value = board.gameStats.moves.value.filter { it != move }
                                board.gameStats.executedMoves.value = listOf(move) + board.gameStats.executedMoves.value
                            }) {
                                Text("${move.nbBalls.toString().padStart(2, '0')} - move (${move.sourcePlace.row}, ${move.sourcePlace.col}) to (${move.targetPlace.row}, ${move.targetPlace.col})")
                            }
                        }
                    }
                    Column {
                        board.gameStats.executedMoves.value.mapIndexed { index, move ->
                            Button(onClick = {
                                move.revert(board.places)
                                board.gameStats.executedMoves.value = board.gameStats.executedMoves.value.filter { it != move }
                                board.gameStats.moves.value = listOf(move) + board.gameStats.moves.value
                            }) {
                                Text("${move.nbBalls.toString().padStart(2, '0')} - revert move (${move.sourcePlace.row}, ${move.sourcePlace.col}) to (${move.targetPlace.row}, ${move.targetPlace.col})")
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
@Preview
fun PlaceView(place: Place = Place(0, 0), allPlaces: List<List<Place>> = emptyList()) {
    val placeHasBall by remember { place.hasBall }
    Button(
        onClick = {
            place.hasBall.value = !place.hasBall.value
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor =
            if (place.checkPossibleMoves(allPlaces, 0).isNotEmpty()) Color.Yellow
            else if (place.hasBall.value) Color.Blue else Color.Cyan
        )
    ) {
        Text(if (placeHasBall) "o" else "")
    }
}


