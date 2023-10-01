package testcoroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun main() {
    println("Start")
    println("Before")
    val beforeAfter : Continuation<Unit>
    suspendCoroutine<Unit> { continuation ->
        println("Before too")
        beforeAfter = continuation
        beforeAfter.resume(Unit)
    }

    println("After")
    val afterAfter : Continuation<BigInteger>
    val res = suspendCoroutine<BigInteger> { continuation ->
        println("After After")
        afterAfter = continuation
        afterAfter.resume(10.toBigInteger())
    }
    println("res : ${res}")
    println("End")
}