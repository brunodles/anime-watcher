package brunodles.animesproject

import brunodles.animewatcher.explorer.*
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object AnimesProjectFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("animes.zlx.com.br/exibir/\\d+/\\d+")
    private val HOST = "http://animes.zlx.com.br"
    private val TITLE_PATTERN = Pattern.compile("(?:(.*?)\\s:\\s)?(.*?)\\s(\\d+)")

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.fetchUrl(url)
        val iframe = doc.select(".video-placeholder iframe").attr("src")
        val src = UrlFetcher.fetchUrl(HOST + iframe).select(".video-js").attr("src")
        val title = doc.select(".serie-pagina-subheader span").text()
        val (animeName, description, number) = splitTitle(title)
        return Episode(animeName = animeName,
                description = "$description $number",
                number = number,
                video = src,
                link = url,
                nextEpisodes = nextEpisodes(doc))
    }

    private fun splitTitle(title: String?): Triple<String?, String, Int> {
        val matcher = TITLE_PATTERN.matcher(title)
        matcher.find()
        val animeName = matcher.group(1) ?: null
        val description = matcher.group(2)
        val number = matcher.group(3).toIntOrNull() ?: 0
        return Triple(animeName, description, number)
    }

    fun nextEpisodes(doc: Document): List<Episode> {
        return doc.select(".exibir-pagina-listagem a").map {
            val (_, _, number) = splitTitle(it.text())
            Episode(description = it.text(),
                    number = number,
                    link = HOST + it.attr("href"))
        }.toList().subList(3, 5)
    }
}