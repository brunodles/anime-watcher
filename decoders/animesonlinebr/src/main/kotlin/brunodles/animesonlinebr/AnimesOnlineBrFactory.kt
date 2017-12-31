package brunodles.animesonlinebr

import brunodles.animewatcher.explorer.*
import org.jsoup.nodes.Document

object AnimesOnlineBrFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("https?://www\\.animesonlinebr\\.com\\.br/video/\\d+")
    private val NUMBER_REGEX = Regex("\\d+")
    private val INVALID_TEXT = arrayOf("assistir", "online")

    override fun isEpisode(url: String): Boolean = url.matches(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.fetchUrl(url)
        val texts = texts(doc)
        val videoSrc = doc.select("video").src()
        val number = texts[1].split(" ").last().toIntOrNull() ?: 0

        return Episode(number = number,
                animeName = texts[0],
                description = texts[1],
                video = videoSrc,
                link = url,
                nextEpisodes = arrayListOf(
                        Episode(
                                number = number + 1,
                                animeName = texts[0],
                                description = doc.select("a.seta2")
                                        .attr("title")
                                        .split("-")
                                        .last()
                                        .trim(),
                                link = doc.select("a.seta2").href()
                        )
                ))
    }

    private fun texts(doc: Document): List<String> {
        var title = doc.title()
        INVALID_TEXT.forEach {
            title = title.replace(it, "", true)
        }
        return title.split("-").map { it.trim() }
    }
}
