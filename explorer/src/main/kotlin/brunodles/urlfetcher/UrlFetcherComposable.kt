package brunodles.urlfetcher

import org.jsoup.nodes.Document

class UrlFetcherComposable internal constructor(val url: String) : UrlFetcher {

    private var internalFetcher: UrlFetcher = JsoupFetcher(url)

    fun withCache(): UrlFetcherComposable {
        internalFetcher = CacheFetcher(url, internalFetcher)
        return this
    }

    fun withRedirect(): UrlFetcherComposable {
        internalFetcher = RedirectFetcher(internalFetcher)
        return this
    }

    override fun post(): Document = internalFetcher.post()

    override fun get(): Document = internalFetcher.get()

}