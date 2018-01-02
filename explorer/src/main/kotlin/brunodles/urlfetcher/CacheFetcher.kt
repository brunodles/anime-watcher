package brunodles.urlfetcher

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

internal class CacheFetcher(private val url: String, private val nestedFetcher: UrlFetcher) :
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
            Logger.log { "savePage $key" }
            file(key).outputStream().bufferedWriter().use { it.write(page) }
        }

        private fun loadPage(key: String): String {
            Logger.log { "loadPage $key" }
            return file(key).inputStream().bufferedReader().use { it.readText() }
        }

        private fun urlToKey(url: String): String = url.replace(INVALID_TEXT_PATTERN, "")
                .max(MAX_FILENAME_SIZE)

        private fun file(key: String): File {
            val dir = File(UrlFetcher.cacheDir, "cache")
            if (!dir.exists())
                dir.mkdirs()
            return File(dir, key)
        }
    }

}