package brunodles.urlfetcher

import brunodles.animewatcher.explorer.BuildConfig
import org.jsoup.nodes.Document

interface UrlFetcher {

    fun post(): Document
    fun get(): Document

    companion object {
        var useCache: Boolean = BuildConfig.USE_CACHE
        var cacheDir = BuildConfig.ROOT_DIR
        var useLog = false

        fun fetcher(url: String): UrlFetcher {
            val fetcher: UrlFetcher = JsoupFetcher(url)
            return if (useCache)
                CacheFetcher(url, fetcher)
            else
                RedirectFetcher(fetcher)
        }

        fun fetchUrl(url: String): Document = fetcher(url).get()
    }
}