package testcoroutine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun logMessage(msg: String) {
    println("${Instant.now()} - ${Thread.currentThread().name} - ${Thread.currentThread().id} $msg")
}

var continuation: Continuation<Unit>? = null
var continuation2: Continuation<Unit>? = null

suspend fun suspendAndSetContinuation() {
    logMessage("inside suspended suspendAndSetContinuation")
    suspendCoroutine<Unit> { cont ->
        logMessage("set continuation")
        continuation = cont
        logMessage("end of set continuation")
    }
    logMessage("after suspendCoroutine in suspendAndSetContinuation")
}

suspend fun suspendAndSetContinuation2() {
    logMessage("inside suspended suspendAndSetContinuation2")
    suspendCoroutine<Unit> { cont ->
        logMessage("set continuation2")
        continuation2 = cont
        logMessage("end of set continuation2")
    }
    logMessage("after suspendCoroutine in suspendAndSetContinuation2")
}


suspend fun main() = coroutineScope {
    logMessage("Start")
    repeat(1) {
        launch {
            logMessage("delay 1000")
            delay(1000)
            logMessage("delay expired")
            logMessage("resume continuation")
            continuation?.resume(Unit)
            logMessage("resume continuation2")
            continuation2?.resume(Unit)
        }
    }
    logMessage("go to suspend")
    suspendAndSetContinuation()
    logMessage("suspended routine 1 has been resume")
    suspendAndSetContinuation2()
    logMessage("suspended routine 2 has been resume")
    logMessage("End")
}