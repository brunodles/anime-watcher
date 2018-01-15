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

        @Deprecated("This does not support composable configuration", ReplaceWith("composableFetcher"))
        fun fetcher(url: String): UrlFetcher = composableFetcher(url).withRedirect()

        fun composableFetcher(url: String): UrlFetcherComposable {
            val urlFetcherComposable = UrlFetcherComposable(url)
            if (useCache)
                urlFetcherComposable.withCache()
            return urlFetcherComposable
        }

        @Deprecated("This does not support composable configuration and always use get", ReplaceWith("composableFetcher"))
        fun fetchUrl(url: String): Document = fetcher(url).get()
    }
}