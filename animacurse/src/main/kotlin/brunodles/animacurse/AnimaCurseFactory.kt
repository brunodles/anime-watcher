package brunodles.animacurse

import brunodles.animewatcher.explorer.*
import org.jsoup.nodes.Document

object AnimaCurseFactory : AnimeFactory {

    private val URL_REGEX = Regex("animacurse\\.moe/?\\?p=")

    override fun isEpisode(url: String): Boolean =
            url.contains(URL_REGEX)

    override fun episode(url: String): AnimeExplorer {
        val doc = UrlFetcher.fetchUrl(url)
        return AnimeExplorer(currentEpisode(doc), nextEpisodes(doc))
    }

    private fun currentEpisode(doc: Document): CurrentEpisode {
        val src = doc.select("video source").first().attr("src")
        val text = doc.select(".anime-info h2").first().text()
        return CurrentEpisode(src, text)
    }

    private fun nextEpisodes(doc: Document): List<EpisodeLink> {
        return doc.select(".thumbnail").map {
            val src = it.select(".holder img").first().attr("src")
            val text = it.select(".text span").first().text()
            val link = it.select(".text a").first().attr("href")
            EpisodeLink(link, text, src)
        }.toList()
    }

}