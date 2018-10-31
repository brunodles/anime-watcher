package brunodles.urlfetcher

import org.jsoup.nodes.Document

class UrlFetcherComposable internal constructor() : UrlFetcher {
    private var internalFetcher: UrlFetcher = JsoupFetcher()

    fun withCache(): UrlFetcherComposable {
        internalFetcher = CacheFetcher(internalFetcher)
        return this
    }

    fun withJsRedirect(): UrlFetcherComposable {
        internalFetcher = RedirectFetcher(internalFetcher)
        return this
    }

    override fun get(url: String): Document = internalFetcher.get(url)
}
