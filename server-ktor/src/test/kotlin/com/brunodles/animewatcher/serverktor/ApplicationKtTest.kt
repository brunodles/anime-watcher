package com.brunodles.animewatcher.serverktor

import brunodles.animewatcher.explorer.Episode
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.parametersOf
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.eclipse.jetty.util.UrlEncoded
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ApplicationKtTest {

    private val animeWatcherApplication: Application.() -> Unit = {
        animewatcher()
    }

    @Test
    fun whenDecode_withoutUrlParameter_shouldReturnBadRequest() = application {
        with(handleRequest(HttpMethod.Get, "/v1/decoder")) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    @Test
    fun whenDecode_withInvalidUrl_shouldReturnBadRequest() = application {
        val handleRequest = handleRequest(HttpMethod.Get, "/v1/decoder") {
            parametersOf("url", "kljsadlkjdsa")
        }
        with(handleRequest) {
            assertEquals(HttpStatusCode.BadRequest, response.status())
        }
    }

    @Test
    fun whenInvalidPath_shouldReturnNotFound() = application {
        with(handleRequest(HttpMethod.Get, "/123")) {
            assertEquals(HttpStatusCode.NotFound, response.status())
        }
    }

    @Test
    fun whenValidUrl_butServerCantBeReached_shouldReturnBadGateway() = withTestApplication({
        animewatcher { url -> null }
    }) {
        val handleRequest = handleUrlRequest("http://validAnimeWebPage.com/linkToEpisode")
        with(handleRequest) {
            assertEquals(HttpStatusCode.BadGateway, response.status())
        }
    }

    @Test
    fun whenValidUrl_shouldReturnEpisodeInfo() = withTestApplication({
        animewatcher { url -> Episode("Description", 10, "animeName", link = "") }
    }) {
        val handleRequest = handleUrlRequest("http://validAnimeWebPage.com/linkToEpisode")
        with(handleRequest) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                "{\"description\":\"Description\",\"number\":10,\"animeName\":\"animeName\",\"link\":\"\",\"nextEpisodes\":[],\"temporaryVideoUrl\":false}",
                response.content
            )
        }
    }

    private fun application(func: TestApplicationEngine.() -> Unit) =
        withTestApplication(animeWatcherApplication, func)

    private fun TestApplicationEngine.handleUrlRequest(url: String) = handleRequest(
        HttpMethod.Get, "/v1/decoder?url=" + UrlEncoded.encodeString(url)
    )
}