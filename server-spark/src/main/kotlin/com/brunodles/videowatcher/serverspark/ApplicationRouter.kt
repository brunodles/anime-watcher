package com.brunodles.videowatcher.serverspark

import brunodles.animewatcher.AlchemistFactory
import brunodles.animewatcher.decoders.UrlChecker
import brunodles.animewatcher.explorer.Episode
import brunodles.urlfetcher.UrlFetcher
import com.google.gson.Gson
import spark.Spark.path
import spark.kotlin.RouteHandler
import spark.kotlin.get
import spark.kotlin.port
import spark.kotlin.post

fun main(args: Array<String>) {
    port(getHerokuAssignedPort())
    AlchemistFactory.setUrlFetcher(UrlFetcher.composableFetcher())
    startServer()
}

val gson = Gson()

fun startServer() {
    // Deprecated
    post("/decoder") {
        decoder { body() }
    }
    path("/v1") {
        get("/decoder") {
            decoder { queryParams("url") }
        }
    }
}

private fun RouteHandler.decoder(urlParameter: spark.Request.() -> String): Any {
    val episode: Episode?
    response.type("Application/json;charset=utf-8")
    try {
        episode = UrlChecker.videoInfo(urlParameter.invoke(request))
    } catch (e: Exception) {
        e.printStackTrace()
        this.response.status(500)
        return gson.toJson(e)
    }
    return gson.toJson(episode)
}

fun getHerokuAssignedPort(): Int = System.getenv("PORT")?.toInt() ?: 4567
