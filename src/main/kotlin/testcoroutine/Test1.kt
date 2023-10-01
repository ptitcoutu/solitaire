package testcoroutine

import kotlin.concurrent.thread

fun main() {
    println("start")
    repeat(100_000) {
        thread {
            Thread.sleep(1000L)
            print(".")
        }
    }
    println("end")
}