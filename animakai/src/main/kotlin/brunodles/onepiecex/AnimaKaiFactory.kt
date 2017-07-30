package brunodles.onepiecex

import brunodles.animewatcher.explorer.*
import org.jsoup.nodes.Document

object AnimaKaiFactory : AnimeFactory {

    private val EPISODE_URL_REGEX = Regex("animakai\\.info/anime/\\d+/episodio-\\d+")
    private val HOST = "https://www.animakai.info"

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): AnimeExplorer {
        val doc = UrlFetcher.fetchUrl(url)
        return AnimeExplorer(findCurrentEpisode(doc), findNextEpisodes(doc))
    }

    private fun findCurrentEpisode(doc: Document): CurrentEpisode {
        val video = doc.select(".box-video video")
        val text = video.alt()
        val src = video.select("source").src()
        return CurrentEpisode(src, text)
    }

    private fun findNextEpisodes(doc: Document): List<EpisodeLink> {
        return doc.select("a.btn-nav-episodios.next").map {
            val text = it.text()
            val link = HOST + it.href()
            EpisodeLink(link, text)
        }.toList()
    }
}
