package brunodles.animacurse;

import bruno.animewatcher.explorer.AnimeExplorer
import bruno.animewatcher.explorer.CurrentEpisode
import bruno.animewatcher.explorer.EpisodeLink
import bruno.animewatcher.explorer.UrlFetcher.Companion.fetchUrl

class AnimaCurseExplorer(private val url: String) : AnimeExplorer {

    private val doc = fetchUrl(url)

    override fun currentEpisode(): CurrentEpisode {
        val src = doc.select("video source").first().attr("src")
        val text = doc.select(".anime-info h2").first().text()
        return CurrentEpisode(src, text)
    }

    override fun nextEpisodes(): List<EpisodeLink> {
        return doc.select(".thumbnail").map {
            val src = it.select(".holder img").first().attr("src")
            val text = it.select(".text span").first().text()
            val link = it.select(".text a").first().attr("href")
            EpisodeLink(link, text, src)
        }.toList()
    }
}