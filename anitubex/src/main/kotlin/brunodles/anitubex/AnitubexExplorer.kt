package brunodles.anitubex

import bruno.animewatcher.explorer.*
import java.util.regex.Pattern

class AnitubexExplorer(private val url: String) : AnimeExplorer {

    companion object {
        private val HREF_REGEX = Pattern.compile("href: \"(.*?)\",")
    }

    private val doc = UrlFetcher.fetchUrl(url)

    override fun currentEpisode(): CurrentEpisode {
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

    override fun nextEpisodes(): List<EpisodeLink> {
        return doc.select(".btn-nav-episodios.next.right")
                .map { EpisodeLink(it.attr("href"), it.text(), null) }
                .toList()
    }
}
