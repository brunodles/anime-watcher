package brunodles.animewatcher.explorer

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.File
import java.net.URLEncoder

class UrlFetcher(private val url: String) {

    private val connection = Jsoup.connect(url)
            .userAgent(USER_AGENT)
            .referrer(REFERRER)
            .timeout(10000)
    private val key by lazy { urlToKey(url) }

    fun post(): Document {
        logger { "fetchUrl \"$url\"" }
        if (useCache && isPageCached(key))
            return Jsoup.parse(loadPage(key))
        val document = connection.post()
        logger { "post \"$url\"" }
        if (useCache)
            savePage(key, document.html())
        return document
    }

    fun get(): Document {
        logger { "fetchUrl \"$url\"" }
        if (useCache && isPageCached(key))
            return Jsoup.parse(loadPage(key))
        val document = connection.get()
        logger { "get \"$url\"" }
        if (useCache)
            savePage(key, document.html())
        return document
    }

    fun data(data: Map<String, String>?): UrlFetcher {
        logger { "data $data" }
        connection.data(data)
        return this
    }

    companion object {
        val UTF8 = "UTF-8"
        private val USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
        private val REFERRER = "http://www.google.com"
        private val INVALID_TEXT_PATTERN = Regex("[^\\d\\w]+")
        private val MAX_FILENAME_SIZE = 100

        var useCache = false
        var useLog = false

        fun fetchUrl(url: String): Document {
            logger { "url: $url" }
            return UrlFetcher(url).get()
        }

        private fun logger(function: () -> String) {
            if (useLog)
                println("UrlFetcher: ${function()}")
        }

        private fun isPageCached(key: String): Boolean = file(key).exists()

        private fun savePage(key: String, page: String) {
            logger { "savePage $key" }
            file(key).outputStream().bufferedWriter().use { it.write(page) }
        }

        private fun loadPage(key: String): String {
            logger { "loadPage $key" }
            return file(key).inputStream().bufferedReader().use { it.readText() }
        }

        private fun urlToKey(url: String): String = url.replace(INVALID_TEXT_PATTERN, "")
                .max(MAX_FILENAME_SIZE)

        private fun file(key: String): File {
            val dir = File(BuildConfig.ROOT_DIR, "cache")
            if (!dir.exists())
                dir.mkdirs()
            return File(dir, key)
        }

    }
}

fun Elements.src() = this.attr("src").trim()
fun Element.src() = this.attr("src").trim()
fun Element.alt() = this.attr("alt")
fun Elements.alt() = this.attr("alt")
fun Elements.href() = this.attr("href")
fun Element.href() = this.attr("href")
fun String.max(max: Int) = this.substring(0, if (length < max) length else max)
fun String.encodeUTF8() = URLEncoder.encode(this, UrlFetcher.UTF8)
