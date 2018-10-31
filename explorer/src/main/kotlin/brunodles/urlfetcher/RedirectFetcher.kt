package brunodles.urlfetcher

import org.jsoup.nodes.Document

internal class RedirectFetcher(
    val nestedFetcher: UrlFetcher,
    val maxRedirects: Int = DEFAULT_MAX_REDIRECTS
) : UrlFetcher {

    companion object {
        private const val DEFAULT_MAX_REDIRECTS = 2
        private val JS_REDIRECT_LOCATION_REGEX =
            Regex("(?:window|self|top)?location(?:\\.href|assign|replace)?\\s*=\\s*[\"'](.*?)[\"'];")
        private val JS_REDIRECT_NAVIGATE_REGEX = Regex("navigateTo\\(.*?[\"'](https?.*?)[\"']\\)")
    }

    override fun get(url: String): Document = get(url, 0)

    private fun get(url: String, level: Int): Document {
        val document = nestedFetcher.get(url)
        if (level >= DEFAULT_MAX_REDIRECTS) return document
        return followRedirect(document, level + 1)
    }

    private fun followRedirect(document: Document, level: Int): Document {
        val redirectNoScript = redirecNoScript(document)
        if (redirectNoScript != null && redirectNoScript.isNotBlank()) {
            Logger.log { "follow redirectNoScript to $redirectNoScript" }
            return get(redirectNoScript, level)
        }

        val redirectJs = redirectJs(document)
        if (redirectJs != null && redirectJs.isNotBlank()) {
            Logger.log { "follow redirectJs to $redirectJs" }
            return get(redirectJs, level)
        }

        return document
    }

    private fun redirecNoScript(document: Document): String? {
        val content = document.head()?.select("noscript meta[http-equiv=refresh]")
            ?.content()
        val split = content?.split("'")
        if (split?.size == 3)
            return split[1]
        return null
    }

    private fun redirectJs(document: Document): String? {
        val text = document.head().html()
        val navigateMatcher = JS_REDIRECT_NAVIGATE_REGEX.find(text)
        if (navigateMatcher?.groups?.size ?: 0 > 1)
            return navigateMatcher?.groupValues?.get(1)
        val locationMatcher = JS_REDIRECT_LOCATION_REGEX.find(text)
        if (locationMatcher?.groups?.size ?: 0 > 1)
            return locationMatcher?.groupValues?.get(1)
        return null
    }
}
