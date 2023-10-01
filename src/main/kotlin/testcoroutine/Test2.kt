package testcoroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("start")
    repeat(100_000) {
        launch {
            delay(1000L)
            print(".")
        }
    }
    println("end")
}