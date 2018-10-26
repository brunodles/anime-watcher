package com.brunodles.animewatcher.serverktor

import brunodles.animewatcher.decoders.UrlChecker
import brunodles.animewatcher.explorer.Episode
import com.google.gson.Gson
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import java.nio.file.ClosedFileSystemException

typealias VideoChecker = (String) -> Episode?

private val gson = Gson()

fun Application.animewatcher(videoChecker: VideoChecker = UrlChecker::videoInfo) {
    installs()
    routing {
        v1Routing(videoChecker)
        route("{...}") {
            handle {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun Application.installs() {
    install(Compression)
    install(CallLogging)
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call.respond(HttpStatusCode.NotFound, "Page not found.") }
    }
}

private fun Routing.v1Routing(videoChecker: VideoChecker) {
    route("v1") {
        get("/decoder") {
            val url = call.parameters["url"]
            if (url == null) {
                call.respond(HttpStatusCode.BadRequest, "Requested url was invalid.")
                return@get
            }
            val episode: Episode? = videoChecker.invoke(url)

            if (episode == null)
                call.respond(HttpStatusCode.BadGateway, "Invalid response from anime page")
            else
                call.respond(HttpStatusCode.OK, gson.toJson(episode))
        }
    }
    intercept(ApplicationCallPipeline.Fallback) {
        throw ClosedFileSystemException()
    }
}
