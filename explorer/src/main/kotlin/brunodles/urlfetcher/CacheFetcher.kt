package brunodles.urlfetcher

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
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

        private val URL_PATTERN =
            Pattern.compile("^(?:.+?\\:\\/\\/)?(?:www\\.)?([\\w\\.\\-\\:]+)(?:[\\/\\?\\&](.+?))?\$")
        private val INVALID_TEXT_PATTERN = Regex("[^\\d\\w]+")
        private const val MAX_FILENAME_SIZE = 100

        private fun isPageCached(key: String): Boolean = file(key).exists()

        private fun savePage(key: String, page: String) {
            Logger.log { "savePage $key" }
            file(key).outputStream().bufferedWriter().use { it.write(page) }
        }

        private fun loadPage(key: String): String {
            Logger.log { "loadPage $key" }
            return file(key).inputStream().bufferedReader().use { it.readText() }
        }

        fun urlToKey(urlStr: String): String {
            val matcher = URL_PATTERN.matcher(urlStr)
            if (!matcher.find())
                throw IllegalArgumentException("Invalid Url parameter: \"$urlStr\"")
            val hostStr = matcher.group(1)
                .replace(Regex("[^\\d\\w.]"), "")
            if (matcher.groupCount() <= 1)
                return hostStr.fixed()+"/_index"
            val path = matcher.group(2) ?: "_index"
            return (hostStr + "/" + path.fixed())
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
    }

    class WrappedException(cachePath: String, cause: Throwable) :
        RuntimeException("Failed to fetch cache using $cachePath", cause)
}
