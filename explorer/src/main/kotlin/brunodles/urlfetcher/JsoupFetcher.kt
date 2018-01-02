package brunodles.urlfetcher

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

internal class JsoupFetcher(private val url: String) : UrlFetcher {

    override fun post(): Document = jsoupConnection().post()

    override fun get(): Document = jsoupConnection().get()

    private fun jsoupConnection() = Jsoup.connect(url)
            .userAgent(USER_AGENT)
            .referrer(REFERRER)
            .timeout(10000)

    companion object {
        private val USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6"
        private val REFERRER = "http://www.google.com"

    }
}