package com.brunodles.videowatcher.serverspark

import brunodles.animewatcher.decoders.UrlChecker
import com.google.gson.Gson
import spark.kotlin.port
import spark.kotlin.post

fun main(args: Array<String>) {
    val gson = Gson()
    port(getHerokuAssignedPort());
    post("/decoder") {
        val episode = UrlChecker.videoInfo(request.body())
        gson.toJson(episode)
    }
}

private fun getHerokuAssignedPort(): Int = System.getenv("PORT")?.toInt() ?: 4567