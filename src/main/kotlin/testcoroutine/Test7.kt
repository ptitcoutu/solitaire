package testcoroutine

import kotlinx.coroutines.delay
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val executor = Executors.newSingleThreadScheduledExecutor {
    println("create thread")
    Thread(it, "scheduler").apply { isDaemon = true }
}

suspend fun delay(time: Long): Unit =
    suspendCoroutine { cont ->
        println("suspend with continuation")
        executor.schedule({
            println("start resume")
            cont.resume(Unit)
            println("end resume")
        }, time, TimeUnit.MILLISECONDS)
    }

suspend fun main() {
    println("Before")
    delay(1_000)
    println("After")
}