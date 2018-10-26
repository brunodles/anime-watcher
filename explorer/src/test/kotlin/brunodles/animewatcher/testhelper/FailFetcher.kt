package brunodles.animewatcher.testhelper

import brunodles.urlfetcher.UrlFetcher
import org.jsoup.nodes.Document

internal class FailFetcher : UrlFetcher {
    override fun post(url: String): Document {
        throw RuntimeException("Failed to fetch: $url")
    }

    override fun get(url: String): Document {
        throw RuntimeException("Failed to fetch: $url")
    }
}