package brunodles.animewatcher.testhelper

import brunodles.urlfetcher.UrlFetcher
import org.jsoup.nodes.Document

internal class FailFetcher : UrlFetcher {

    override fun get(url: String): Document {
        throw FetchingException("Failed to fetch: $url")
    }

    class FetchingException internal constructor(msg: String) : RuntimeException(msg)
}
