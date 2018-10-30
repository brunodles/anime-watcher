package brunodles.animewatcher.testhelper

import brunodles.loadResource
import brunodles.urlfetcher.Logger
import brunodles.urlfetcher.UrlFetcher
import brunodles.urlfetcher.max
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL
import java.util.regex.Pattern

internal class ResourceFetcher(private val nestedFetcher: UrlFetcher) : UrlFetcher {

    override fun get(url: String): Document {
        val key = urlToKey(url)
        try {
            return Jsoup.parse(loadPage(key))
        } catch (_: Exception) {
        }
        try {
            return nestedFetcher.get(url)
        } catch (e: Throwable) {
            throw WrappedException(key, e)
        }
    }

    companion object {

        private val DOMAIN_PATTERN =
            Pattern.compile("^(?:.*?)([\\w\\d-]+(?:\\.\\w{2,5}))(?:\\.\\w{2,4})?\$")
        private val INVALID_TEXT_PATTERN = Regex("[^\\d\\w]+")
        private val MAX_FILENAME_SIZE = 100

        private fun loadPage(key: String): String {
            Logger.log { "loadPage $key" }
            return loadResource(key)
        }

        private fun urlToKey(urlStr: String): String {
            val url = URL(urlStr)
            val hostStr = extractDomain(url)
                .replace(Regex("[^\\d\\w.]"), "")
            return (hostStr + "/" + url.path.fixed() + url.query.fixed())
                .max(MAX_FILENAME_SIZE)
        }

        private fun String?.fixed() = this?.replace(INVALID_TEXT_PATTERN, "") ?: ""

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
