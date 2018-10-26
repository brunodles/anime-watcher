package brunodles.animewatcher.testhelper

import brunodles.urlfetcher.UrlFetcher
import org.jsoup.nodes.Document

internal class RetryFetcher(private val fetcher: UrlFetcher) : UrlFetcher {
    override fun post(url: String): Document {
        return try {
            fetcher.post(url)
        } catch (e: Exception) {
            e.printStackTrace(System.err)
            fetcher.post(url)
        }
    }

    override fun get(url: String): Document {
        return try {
            fetcher.get(url)
        } catch (e: Exception) {
            e.printStackTrace(System.err)
            fetcher.get(url)
        }
    }
}