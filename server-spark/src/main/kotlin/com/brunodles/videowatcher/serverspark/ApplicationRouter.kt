package com.brunodles.videowatcher.serverspark

import brunodles.animewatcher.decoders.UrlChecker
import brunodles.animewatcher.explorer.Episode
import brunodles.urlfetcher.UrlFetcher
import com.google.gson.Gson
import spark.kotlin.port
import spark.kotlin.post

fun main(args: Array<String>) {
    UrlFetcher.useCache = false
    port(getHerokuAssignedPort())
    startServer()
}

fun startServer() {
    val gson = Gson()
    post("/decoder") {
        val episode: Episode?
        try {
            episode = UrlChecker.videoInfo(request.body())
        } catch (e: Exception) {
            e.printStackTrace()
            this.response.status(500)
            return@post gson.toJson(e)
        }
        gson.toJson(episode)
    }
}

private fun getHerokuAssignedPort(): Int = System.getenv("PORT")?.toInt() ?: 4567