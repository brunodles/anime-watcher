package brunodles.urlfetcher

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.net.URL
import java.util.regex.Pattern

internal class CacheFetcher(private val nestedFetcher: UrlFetcher) : UrlFetcher {

    override fun get(url: String): Document {
        val key = urlToKey(url)
        if (isPageCached(key))
            return Jsoup.parse(loadPage(key))
        try {
            val document: Document = nestedFetcher.get(url)
            savePage(key, document.html())
            return document
        } catch (e: Throwable) {
            throw WrappedException(key, e)
        }
    }

    companion object {

        private val DOMAIN_PATTERN =
            Pattern.compile("^(?:.*?)([\\w\\d-]+(?:\\.\\w{2,5}))(?:\\.\\w{2,4})?\$")
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

        private fun urlToKey(urlStr: String): String {
            val url = URL(urlStr)
            val hostStr = extractDomain(url)
                .replace(Regex("[^\\d\\w.]"), "")
            return (hostStr + "/" + url.path.fixed() + url.query.fixed())
                .max(MAX_FILENAME_SIZE)
        }

        private fun String?.fixed() = this?.replace(INVALID_TEXT_PATTERN, "") ?: ""

        private fun file(key: String): File {
            val dir = File(UrlFetcher.cacheDir, "cache")
            if (!dir.exists())
                dir.mkdirs()
            if (key.contains('/')) {
                val split = key.split('/')
                val hostDirName = split[0]
                val hostDir = File(dir, hostDirName)
                hostDir.mkdirs()
                return File(hostDir, split[1])
            }
            return File(dir, key)
        }

        fun extractDomain(url: String) = extractDomain(URL(url))
        fun extractDomain(url: URL): String {
            val matcher = DOMAIN_PATTERN.matcher(url.host)
            return if (matcher.find())
                matcher.group(1)
            else
                url.host
        }
    }

    class WrappedException(cachePath: String, cause: Throwable) :
        RuntimeException("Failed to fetch cache using $cachePath", cause)
}
