package brunodles.animacurse

import brunodles.animewatcher.explorer.PageExplorer
import brunodles.animewatcher.explorer.PageParser
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.FACTORIES
import brunodles.animewatcher.explorer.UrlFetcher
import org.jsoup.nodes.Document

object AnimaCurseFactory : PageParser {

    private val URL_REGEX = Regex("animacurse\\.moe/?\\?p=")

    init {
        FACTORIES.add(this)
    }

    override fun isEpisode(url: String): Boolean =
            url.contains(URL_REGEX)

    override fun episode(url: String): PageExplorer {
        val doc = UrlFetcher.fetchUrl(url)
        return PageExplorer(currentEpisode(doc, url), nextEpisodes(doc))
    }

    private fun currentEpisode(doc: Document, url: String): Episode {
        val src = doc.select("video source").first().attr("src")
        val text = doc.select(".episodename h1").first().text()
        return Episode(link = url, video = src, description = text)
    }

    private fun nextEpisodes(doc: Document): List<Episode> {
        return doc.select(".episode").map {
            val src = it.select("#epimg img").first().attr("src")
            val text = it.select("#epnum").first().text()
            val link = it.select("a").first().attr("href")
            Episode(link = link, description = text, image = src)
        }.toList()
    }

}

