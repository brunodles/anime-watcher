package brunodles.onepiecex

import bruno.animewatcher.explorer.*
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object OnePieceXFactory : AnimeFactory {

    private val EPISODE_URL_REGEX = Regex("one-piece-x.com.br/episodios/online/\\d+")
    private val EPISODE_FINDER_PATTERN = Pattern.compile("\"(\\d+)p\"\\s*:\\s*\"(.*?)\"")
    private val HOST = "https://one-piece-x.com.br"

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): AnimeExplorer {
        val doc = UrlFetcher.fetchUrl(url)
        return AnimeExplorer(findCurrentEpisode(doc), findNextEpisodes(doc))
    }

    private fun findCurrentEpisode(doc: Document): CurrentEpisode {
        val text = doc.select("option[selected]").text()
        val iframeLink = "http:" + doc.select("#bannerVideoOnline iframe").src()
        val src = findBestVideoUrl(iframeLink)
        return CurrentEpisode(src, text)
    }

    private fun findBestVideoUrl(iframeLink: String): String {
        val iframeHtml = UrlFetcher.fetchUrl(iframeLink).html()
        val matcher = EPISODE_FINDER_PATTERN.matcher(iframeHtml)
        var maxQuality = 0
        var bestSrc: String? = null
        while (matcher.find()) {
            val quality = matcher.group(1).toInt()
            val src = matcher.group(2)
            if (quality > maxQuality) {
                bestSrc = src
                maxQuality = quality
                continue
            }
        }
        return bestSrc!!
    }

    private fun findNextEpisodes(doc: Document): List<EpisodeLink> {
        return doc.select("select#episodiosLista option").map {
            val text = it.text()
            val link = HOST + it.attr("value")
            EpisodeLink(link, text)
        }.toList()
    }
}
