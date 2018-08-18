package com.brunodles.videowatcher.serverspark

import brunodles.urlfetcher.UrlFetcher
import junit.framework.Assert.assertEquals
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.model.Statement
import spark.kotlin.port
import spark.kotlin.stop
import java.net.URL

@RunWith(JUnit4::class)
class ApplicationRouterKtTest {

    companion object {
        val TEXT_PLAIN = MediaType.parse("text/plain; charset=utf-8")
        const val EXPECTED = "{\"description\":\"Tsukipro The Animation 1\",\"number\":1,\"animeName\":\"Tsukipro The Animation\",\"image\":\"http://www.animekai.info/2017/10/Screenshot_33.jpg\",\"video\":\"http://www.blogger.com/video-play.mp4?contentId\\u003db186c220e9973f58\",\"link\":\"https://www.animekaionline.com/tsukipro-the-animation/episodio-1\",\"nextEpisodes\":[],\"temporaryVideoUrl\":false}"

        @ClassRule
        @JvmField
        val serverRule = TestRule { base, description ->
            object : Statement() {
                override fun evaluate() {
                    UrlFetcher.useCache = true
                    val thread = Thread()
                    thread.run {
                        startServer()
                    }
                    base.evaluate()
                    stop()
                    thread.join()
                }
            }
        }
    }

    @Test
    fun temp() {
        val url = URL("http://localhost:${port()}/decoder")
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .post(RequestBody.create(TEXT_PLAIN, "https://www.animekaionline.com/tsukipro-the-animation/episodio-1"))
                .build()

        val response = client.newCall(request).execute().body()?.bytes()?.let { String(it) }
        assertEquals(EXPECTED, response)
    }
}
