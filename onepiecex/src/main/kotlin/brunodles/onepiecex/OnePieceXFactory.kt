package brunodles.onepiecex

import bruno.animewatcher.explorer.*
import org.jsoup.nodes.Document

object OnePieceXFactory : AnimeFactory {

    private val EPISODE_URL_REGEX = Regex("one-piece-x.com.br/episodios/online/\\d+")
    private val HOST = "https://one-piece-x.com.br"

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): AnimeExplorer {
        val doc = UrlFetcher.fetchUrl(url)
        return AnimeExplorer(findCurrentEpisode(doc), findNextEpisodes(doc))
    }

    private fun findCurrentEpisode(doc: Document): CurrentEpisode {
        val text = doc.select("span#select2-chosen").text()
        val iframeLink = "http:" + doc.select("#bannerVideoOnline iframe").src()
        val src = UrlFetcher.fetchUrl(iframeLink).select("#ad-video").src()
        return CurrentEpisode(src, text)
    }

    private fun findNextEpisodes(doc: Document): List<EpisodeLink> {
        return doc.select("#episodiosLista options").map {
            val text = it.text()
            val link = HOST + it.attr("value")
            EpisodeLink(link, text)
        }.toList()
    }
}
