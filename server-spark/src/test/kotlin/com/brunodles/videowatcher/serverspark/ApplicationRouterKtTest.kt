package com.brunodles.videowatcher.serverspark

import brunodles.animewatcher.AlchemistFactory
import brunodles.urlfetcher.UrlFetcher
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.junit.Assert.assertEquals
import org.junit.ClassRule
import org.junit.Ignore
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.model.Statement
import spark.kotlin.stop
import java.net.URL
import java.net.URLEncoder

@RunWith(JUnit4::class)
@Ignore
class ApplicationRouterKtTest {

    companion object {
        val TEXT_PLAIN = MediaType.parse("text/plain; charset=utf-8")
        const val EXPECTED =
            "{\"description\":\"Tsukipro The Animation 1\",\"number\":1,\"animeName\":\"Tsukipro The Animation\",\"image\":\"http://www.animekai.info/2017/10/Screenshot_33.jpg\",\"video\":\"http://www.blogger.com/video-play.mp4?contentId\\u003db186c220e9973f58\",\"link\":\"https://www.animekaionline.com/tsukipro-the-animation/episodio-1\",\"nextEpisodes\":[],\"temporaryVideoUrl\":false}"
        val PORT = getHerokuAssignedPort()

        @ClassRule
        @JvmField
        val serverRule = TestRule { base, description ->
            object : Statement() {
                override fun evaluate() {
                    AlchemistFactory.urlFetcher = UrlFetcher.composableFetcher().withCache()
                    val thread = Thread()
                    thread.run {
                        startServer()
                    }
                    Thread.sleep(500)
                    base.evaluate()
                    stop()
                    thread.join()
                }
            }
        }

        val client = OkHttpClient()
    }

    @Test
    fun whenRequestDecode_withPost_shouldReturn_expectedValue() {
        val url = URL("http://localhost:$PORT/decoder")
        val request = Request.Builder()
            .url(url)
            .post(
                RequestBody.create(
                    TEXT_PLAIN,
                    "https://www.animekaionline.com/tsukipro-the-animation/episodio-1"
                )
            )
            .build()

        val response = client.call(request)
        assertEquals(EXPECTED, response)
    }

    @Test
    fun whenRequestV1Decode_withGet_shouldReturn_expectedValue() {
        val url = URL(
            "http://localhost:$PORT/v1/decoder?url=" + URLEncoder.encode(
                "https://www.animekaionline.com/tsukipro-the-animation/episodio-1",
                "UTF-8"
            )
        )
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.call(request)
        assertEquals(EXPECTED, response)
    }

    private fun OkHttpClient.call(request: Request) =
        this.newCall(request).execute().body()?.bytes()?.let { String(it) }
}
