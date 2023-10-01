package testcoroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val seq = sequence<Number> {
    println("Generating first")
    yield(1)
    println("Generating second")
    yield(2)
    println("Generating third")
    yield(3)
    println("Done")
}
fun main() = runBlocking {
    println("start")
    for (num in seq) {
        print(num)
    }
    println("end")
}