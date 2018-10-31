package com.brunodles.animewatcher.serverktor

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    val port = getHerokuAssignedPort()
    val server = embeddedServer(Netty, port = port) {
        animewatcher()
    }
    server.start(wait = true)
}

fun getHerokuAssignedPort(): Int = System.getenv("PORT")?.toInt() ?: 4567
