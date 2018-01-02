package brunodles.animesonlinebr

import brunodles.animewatcher.explorer.*
import brunodles.urlfetcher.UrlFetcher
import brunodles.urlfetcher.href
import brunodles.urlfetcher.src
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

        val nextEpisodesUrl = doc.select(".infosingle .left-single a")?.href()

        val nextEpisodes: List<Episode> = if (nextEpisodesUrl != null && nextEpisodesUrl.startsWith("http"))
            try {
                nextEpisodesFromMainPage(nextEpisodesUrl, texts[0])
                        .filter { it.number > number }
            } catch (e:Exception) {
                singleNextEpisode(doc, number, texts[0])
            }
        else
            singleNextEpisode(doc, number, texts[0])

        return Episode(number = number,
                animeName = texts[0],
                description = texts[1],
                video = videoSrc,
                link = url,
                nextEpisodes = nextEpisodes)
    }

    private fun nextEpisodesFromMainPage(nextEpisodesUrl: String, animeName: String): List<Episode> =
            UrlFetcher.fetchUrl(nextEpisodesUrl).select(".lcp_catlist.list li a")
                    .map {
                        Episode(
                                number = NUMBER_REGEX.find(it.text())?.value?.toIntOrNull() ?: 0,
                                description = it.text().split("-").last().trim(),
                                link = it.href(),
                                animeName = animeName
                        )
                    }


    private fun singleNextEpisode(doc: Document, number: Int, animeName: String): List<Episode> =
            arrayListOf(Episode(
                    number = number + 1,
                    animeName = animeName,
                    description = doc.select("a.seta2")
                            .attr("title")
                            .split("-")
                            .last()
                            .trim(),
                    link = doc.select("a.seta2").href()
            ))


    private fun texts(doc: Document): List<String> {
        var title = doc.title()
        INVALID_TEXT.forEach {
            title = title.replace(it, "", true)
        }
        return title.split("-").map { it.trim() }
    }
}
