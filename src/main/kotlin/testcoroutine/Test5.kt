package testcoroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

fun allUsersFlow(): Flow<String> = flow {
    var page = 0
    do {
        page++
        val users = flowOf("test${page}1", "test${page}2")
        emitAll(users)
    } while(page < 10)
}
fun main() = runBlocking {
    println("start")
    allUsersFlow().collect {
        println(it)
    }
    println("end")
}