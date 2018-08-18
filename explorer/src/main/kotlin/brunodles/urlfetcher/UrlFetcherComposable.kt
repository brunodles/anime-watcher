package brunodles.urlfetcher

import org.jsoup.nodes.Document

class UrlFetcherComposable internal constructor() : UrlFetcher {
    private var internalFetcher: UrlFetcher = JsoupFetcher()

    fun withCache(): UrlFetcherComposable {
        internalFetcher = CacheFetcher(internalFetcher)
        return this
    }

    fun withRedirect(): UrlFetcherComposable {
        internalFetcher = RedirectFetcher(internalFetcher)
        return this
    }

    override fun post(url: String): Document = internalFetcher.post(url)

    override fun get(url: String): Document = internalFetcher.get(url)
}
