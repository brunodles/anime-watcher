package brunodles.animesproject

import brunodles.animewatcher.explorer.AnimeExplorer
import brunodles.animewatcher.explorer.AnimeFactory
import brunodles.animewatcher.explorer.CurrentEpisode
import brunodles.animewatcher.explorer.EpisodeLink
import brunodles.animewatcher.explorer.UrlFetcher
import org.jsoup.nodes.Document

object AnimesProjectFactory : AnimeFactory {

    private val EPISODE_URL_REGEX = Regex("animes.zlx.com.br/exibir/\\d+/\\d+")
    private val HOST = "http://animes.zlx.com.br/"

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): AnimeExplorer {
        val doc = UrlFetcher.fetchUrl(url)
        return AnimeExplorer(findCurrentEpisode(doc, url), nextEpisodes(doc))
    }

    private fun findCurrentEpisode(doc: Document, url: String): CurrentEpisode {
        val iframe = doc.select(".video-placeholder iframe").attr("src")
        val src = UrlFetcher.fetchUrl(HOST + iframe).select(".video-js").attr("src")
        val title = doc.select(".serie-pagina-subheader span").text()
        return CurrentEpisode(src, title, url)
    }

    fun nextEpisodes(doc: Document): List<EpisodeLink> {
        return doc.select(".exibir-pagina-listagem a").map {
            EpisodeLink(AnimesProjectFinder.HOST + it.attr("href"), it.text(), null)
        }.toList().subList(3, 5)
    }
}