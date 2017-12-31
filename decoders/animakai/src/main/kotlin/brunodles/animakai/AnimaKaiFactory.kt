package brunodles.animakai

import brunodles.animewatcher.explorer.*
import org.jsoup.nodes.Document

object AnimaKaiFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("https?://www\\.animekaionline\\.com/(.*?)/episodio-\\d+")
    private val NUMBER_REGEX = Regex("\\d+")

    override fun isEpisode(url: String): Boolean = url.matches(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.fetchUrl(url)
        val video = doc.select(".box-video video")
        val text = video.alt()
        val src = video.select("source").src()
        return Episode(number = url.split("-").last().toIntOrNull() ?: 0,
                animeName = animeName(doc),
                image = imageUrl(doc),
                description = text,
                video = src,
                link = url)
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
