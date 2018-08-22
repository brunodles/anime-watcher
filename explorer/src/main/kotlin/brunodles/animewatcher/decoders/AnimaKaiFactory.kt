package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.urlfetcher.UrlFetcher
import brunodles.urlfetcher.src
import org.jsoup.nodes.Document

object AnimaKaiFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("(?:https?:\\/\\/)?(?:www\\.)?((animekaionline|animeskai)\\.com|animakai\\.info)\\/(.*?)\\/(episodio|ep)-\\d+")
    private val NUMBER_REGEX = Regex("\\d+")
    private val urlFetcher = UrlFetcher.fetcher()

    override fun isEpisode(url: String): Boolean = url.matches(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = urlFetcher.get(url)
        val video = doc.select(".box-video video")
        val src = video.select("source").src()
        return Episode(
                number = url.split("-").last().toIntOrNull() ?: 0,
                animeName = animeName(doc),
                image = imageUrl(doc),
                description = doc.select("[property=og:description]").attr("content"),
                video = src,
                link = url
        )
    }

    private fun imageUrl(doc: Document): String? {
        val url = doc.head()
                .select("meta[property=og:image]")
                ?.attr("content")
                ?.split("http")
                ?.last()
        if (url != null)
            return "http$url"
        return null
    }

    private fun animeName(doc: Document): String? =
            doc.head()
                    .select("meta[property=og:description]")
                    ?.attr("content")
                    ?.split(" ")
                    ?.filter { !it.matches(NUMBER_REGEX) }
                    ?.joinToString(" ")
}
