package testcoroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

val fibonacci = sequence<BigInteger> {
    var first = 0.toBigInteger()
    var second = 1.toBigInteger()
    while (true) {
        yield(first)
        val temp = first
        first += second
        second = temp
    }
}
fun main() = runBlocking {
    println("start")
    print(fibonacci.take(2000).toList())
    println("end")
}