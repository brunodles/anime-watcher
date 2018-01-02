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

        fun fetchUrl(url: String): Document {
            var fetcher: UrlFetcher = JsoupFetcher(url)
            if (useCache)
                fetcher = CacheFetcher(url, fetcher)
            fetcher = RedirectFetcher(fetcher)
            return fetcher.get()
        }
    }
}