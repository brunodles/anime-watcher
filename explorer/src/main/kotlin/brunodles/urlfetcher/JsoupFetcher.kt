package brunodles.urlfetcher

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

internal class JsoupFetcher : UrlFetcher {

    override fun get(url: String): Document = jsoupConnection(url).get()

    private fun jsoupConnection(url: String) = Jsoup.connect(url)
        .userAgent(USER_AGENT)
        .referrer(REFERRER)
        .timeout(10000)
        .followRedirects(true)
        .ignoreHttpErrors(true)

    companion object {
        private val USER_AGENT =
            "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
        private val REFERRER = "http://www.google.com"
    }
}
