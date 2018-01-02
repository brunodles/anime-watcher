package brunodles.anitubex

import brunodles.animewatcher.explorer.*
import brunodles.urlfetcher.UrlFetcher
import brunodles.urlfetcher.href
import brunodles.urlfetcher.src
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object AnitubexFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("anitubex.com/.*?\\d+")
    private val HREF_REGEX = Pattern.compile("href:\\s?\"(.*?)\",")
    private val SPACES = Pattern.compile("\\s")

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.fetchUrl(url)

        val text = doc.select(".panel-heading h1").text()
        var iframeLink = doc.select(".tab-pane iframe").src()
        val number = extractNumberFromText(text)
        val nextEpisodes = findNextEpisodes(doc, number + 1)

        var iframe = UrlFetcher.fetchUrl(iframeLink)
        do {
            iframeLink = iframe.select("iframe").src()
            if (iframeLink.isNullOrEmpty()) {
                val matcher = HREF_REGEX.matcher(iframe.html())
                if (matcher.find())

                    return Episode(
                            description = text,
                            number = number,
                            video = matcher.group(1),
                            link = url,
                            nextEpisodes = nextEpisodes)
                break
            }
            iframe = UrlFetcher.fetchUrl(iframeLink)
        } while (iframeLink != null)
        val videoUrl = iframe.select("#advideox").src()

        return Episode(
                description = text,
                number = number,
                video = videoUrl,
                link = url,
                nextEpisodes = nextEpisodes)
    }

    private fun extractNumberFromText(text: String): Int {
        val split = text.split(SPACES)
        val last = split.last()
        return last.toIntOrNull() ?: 0
    }

    private fun findNextEpisodes(doc: Document, number: Int): List<Episode> {
        return doc.select(".btn-nav-episodios.next")
                .map {
                    Episode(description = it.text(),
                            number = it.href().split("-").last().toIntOrNull() ?: number,
                            link = it.href())
                }.toList()
    }
}