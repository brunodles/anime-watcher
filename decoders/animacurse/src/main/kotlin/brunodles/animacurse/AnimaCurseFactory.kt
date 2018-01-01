package brunodles.animacurse

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.urlfetcher.UrlFetcher
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object AnimaCurseFactory : PageParser {

    private val URL_REGEX = Regex("animacurse\\.moe/?\\?p=")
    private val TITLE_REGEX = Pattern.compile(".*?(\\d+)\\s.\\s(.+)")
    private const val IMAGE_URL_FORMAT = "https://animacurse.moe/imgs/%s-episodio-%s.webp"

    override fun isEpisode(url: String): Boolean =
            url.contains(URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.fetchUrl(url)
        val nextEpisodes = nextEpisodes(doc)
        return currentEpisode(doc, url, nextEpisodes)
    }

    private fun currentEpisode(doc: Document, url: String, nextEpisodes: List<Episode>): Episode {
        val src = doc.select("video source").first().attr("src")
        val text = doc.select(".details h2").first().text()
        val animeName = doc.select(".details h1").first().text()
        val (number, description) = splitNumberDescription(text)
        return Episode(number = number,
                description = description,
                animeName = animeName,
                link = url,
                video = src,
                nextEpisodes = nextEpisodes,
                image = String.format(IMAGE_URL_FORMAT, animeName.toLowerCase().replace(Regex("\\s"), "-")
                        , number)
        )
    }

    private fun splitNumberDescription(text: String?): Pair<Int, String> {
        val matcher = TITLE_REGEX.matcher(text)
        matcher.find()
        val number = matcher.group(1).toInt()
        val description = matcher.group(2)
        return Pair(number, description)
    }

    private fun nextEpisodes(doc: Document): List<Episode> {
        return doc.select(".thumbnail").map {
            val img = it.select("img").first().attr("src")
            val text = it.select(".text a.title span.episode").first().text()
            val animeName = it.select(".text a.title div.limit").first().text()
            val link = it.select(".overlay a.play").first().attr("href")
            val (number, description) = splitNumberDescription(text)
            Episode(number = number,
                    description = description,
                    animeName = animeName,
                    link = link,
                    image = img
            )
        }.toList()
    }

}

