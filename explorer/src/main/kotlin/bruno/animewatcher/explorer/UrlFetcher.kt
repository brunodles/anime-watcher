package bruno.animewatcher.explorer

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class UrlFetcher {

    companion object {
        var useCache = false
        private val USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
        private val REFERRER = "http://www.google.com"
        private val INVALID_TEXT_PATTERN = Regex("[^\\d\\w]+")

        fun fetchUrl(url: String): Document {
            if (useCache)
                return fetchUrlWithCacheControl(url)
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .referrer(REFERRER)
                    .get()
        }

        private fun fetchUrlWithCacheControl(url: String): Document {
            val key = urlToKey(url)
            val page: String
            if (isPageCached(key)) {
                page = loadPage(key)
            } else {
                page = downloadPage(url)
                savePage(key, page)
            }
            return Jsoup.parse(page)
        }

        private fun isPageCached(key: String): Boolean = file(key).exists()

        private fun savePage(key: String, page: String) =
                file(key).outputStream().bufferedWriter().use { it.write(page) }

        private fun loadPage(key: String): String =
                file(key).inputStream().bufferedReader().use { it.readText() }

        private fun urlToKey(url: String): String = url.replace(INVALID_TEXT_PATTERN, "")

        private fun downloadPage(url: String): String {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", USER_AGENT)
            return connection.inputStream.bufferedReader().use { it.readText() }
        }

        private fun file(key: String): File {
            val dir = File("./cache")
            if (!dir.exists())
                dir.mkdirs()
            return File(dir, key)
        }

    }

}