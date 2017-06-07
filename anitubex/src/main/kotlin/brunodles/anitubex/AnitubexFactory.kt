package brunodles.anitubex

import bruno.animewatcher.explorer.*
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object AnitubexFactory : AnimeFactory {

    private val EPISODE_URL_REGEX = Regex("anitubex.com/.*?\\d+")
    private val HREF_REGEX = Pattern.compile("href: \"(.*?)\",")

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): AnimeExplorer {
        val doc = UrlFetcher.fetchUrl(url)
        val currentEpisode = findCurrentEpisode(doc)
        val nextEpisodes = findNextEpisodes(doc)
        return AnimeExplorer(currentEpisode, nextEpisodes)
    }

    private fun findCurrentEpisode(doc: Document): CurrentEpisode {
        val text = doc.select(".panel-heading h1").text()
        var iframeLink = doc.select(".tab-pane iframe").src()
        var iframe = UrlFetcher.fetchUrl(iframeLink)
        do {
            iframeLink = iframe.select("iframe").src()
            if (iframeLink.isNullOrEmpty()) {
                val matcher = HREF_REGEX.matcher(iframe.html())
                if (matcher.find())
                    return CurrentEpisode(matcher.group(1), text)
                break
            }
            iframe = UrlFetcher.fetchUrl(iframeLink)
        } while (iframeLink != null)
        val link = iframe.select("#advideox").src()
        return CurrentEpisode(link, text)
    }

    private fun findNextEpisodes(doc: Document): List<EpisodeLink> {
        return doc.select(".btn-nav-episodios.next.right")
                .map { EpisodeLink(it.attr("href"), it.text(), null) }
                .toList()
    }
}