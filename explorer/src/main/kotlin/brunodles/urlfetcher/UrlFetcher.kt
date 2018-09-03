package brunodles.urlfetcher

import brunodles.animewatcher.explorer.BuildConfig
import org.jsoup.nodes.Document

/**
 * A Class to fetch urls.
 */
interface UrlFetcher {

    fun post(url: String): Document
    fun get(url: String): Document

    companion object {
        val useCache: Boolean = BuildConfig.USE_CACHE
        var cacheDir = BuildConfig.ROOT_DIR
        var useLog = false

        /**
         * Returns an instance of UrlFetcher.
         * This instance may handle caches.
         * @See useCache
         */
        fun fetcher(): UrlFetcher {
            val urlFetcherComposable = composableFetcher()
            if (useCache)
                urlFetcherComposable.withCache()
                    .withJsRedirect()
            return urlFetcherComposable
        }

        /**
         * Returns an instance of a UrlFetcher that can be composable.
         * With it you can compose multiple behaviours of UrlFetcher, like:
         * * Cache
         * * Follow Redirects
         */
        fun composableFetcher() = UrlFetcherComposable()

        @Deprecated(
            "This does not support composable configuration and always use GET",
            ReplaceWith("composableFetcher"), DeprecationLevel.ERROR
        )
        fun fetchUrl(url: String): Document = fetcher().get(url)
    }
}