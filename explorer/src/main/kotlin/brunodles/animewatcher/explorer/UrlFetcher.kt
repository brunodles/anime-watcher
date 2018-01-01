package brunodles.animewatcher.explorer

import brunodles.animewatcher.explorer.Logger.log
import brunodles.animewatcher.explorer.UrlFetcher.Companion.cacheDir
import brunodles.animewatcher.explorer.UrlFetcher.Companion.useLog
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.File
import java.net.URLEncoder

interface UrlFetcher {

    fun post(): Document
    fun get(): Document

    companion object {
        var useCache: Boolean = BuildConfig.USE_CACHE
        var cacheDir = BuildConfig.ROOT_DIR
        var useLog = false

        fun fetchUrl(url: String): Document {
            var fetcher: UrlFetcher = JsoupFetcher(url)
            if (useCache)
                fetcher = CacheFetcher(url, fetcher)
            return fetcher.get()
        }
    }
}

private class CacheFetcher(private val url: String, private val nestedFetcher: UrlFetcher) :
        UrlFetcher {

    private val key by lazy { urlToKey(url) }

    override fun post(): Document {
        if (isPageCached(key))
            return Jsoup.parse(loadPage(key))
        val document = nestedFetcher.post()
        savePage(key, document.html())
        return document
    }

    override fun get(): Document {
        if (isPageCached(key))
            return Jsoup.parse(loadPage(key))
        val document = nestedFetcher.get()
        savePage(key, document.html())
        return document
    }

    companion object {

        private val INVALID_TEXT_PATTERN = Regex("[^\\d\\w]+")
        private val MAX_FILENAME_SIZE = 100

        private fun isPageCached(key: String): Boolean = file(key).exists()

        private fun savePage(key: String, page: String) {
            log { "savePage $key" }
            file(key).outputStream().bufferedWriter().use { it.write(page) }
        }

        private fun loadPage(key: String): String {
            log { "loadPage $key" }
            return file(key).inputStream().bufferedReader().use { it.readText() }
        }

        private fun urlToKey(url: String): String = url.replace(INVALID_TEXT_PATTERN, "")
                .max(MAX_FILENAME_SIZE)

        private fun file(key: String): File {
            val dir = File(cacheDir, "cache")
            if (!dir.exists())
                dir.mkdirs()
            return File(dir, key)
        }
    }

}

private class JsoupFetcher(private val url: String) : UrlFetcher {

    override fun post(): Document = jsoupConnection().post()

    override fun get(): Document = jsoupConnection().get()

    private fun jsoupConnection() = Jsoup.connect(url)
            .userAgent(USER_AGENT)
            .referrer(REFERRER)
            .timeout(10000)

    companion object {
        private val USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
        private val REFERRER = "http://www.google.com"

    }
}

object Logger {

    internal fun log(function: () -> String) {
        if (useLog)
            println("UrlFetcher: ${function()}")
    }
}

private val UTF8 = "UTF-8"
fun Elements.src() = this.attr("src").trim()
fun Element.src() = this.attr("src").trim()
fun Element.alt() = this.attr("alt")
fun Elements.alt() = this.attr("alt")
fun Elements.href() = this.attr("href")
fun Element.href() = this.attr("href")
fun String.max(max: Int) = this.substring(0, if (length < max) length else max)
fun String.encodeUTF8() = URLEncoder.encode(this, UTF8)
