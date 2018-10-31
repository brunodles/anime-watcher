package brunodles.animewatcher.testhelper

import brunodles.loadResource
import brunodles.urlfetcher.CacheFetcher.Companion.urlToKey
import brunodles.urlfetcher.Logger
import brunodles.urlfetcher.UrlFetcher
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

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

        private fun loadPage(key: String): String {
            Logger.log { "loadPage $key" }
            return loadResource(key)
        }
    }

    class WrappedException(cachePath: String, cause: Throwable) :
        RuntimeException("Failed to fetch cache using $cachePath", cause)
}
