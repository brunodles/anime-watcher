package brunodles.urlfetcher

import org.jsoup.nodes.Document

internal class RedirectFetcher(val nestedFetcher: UrlFetcher) : UrlFetcher {
    override fun post(): Document {
        val document = nestedFetcher.post()
        return document
    }

    override fun get(): Document {
        val document = nestedFetcher.get()
        return document
    }
}