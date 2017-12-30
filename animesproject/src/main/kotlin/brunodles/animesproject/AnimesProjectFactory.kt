package brunodles.animesproject

import brunodles.animewatcher.explorer.*
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object AnimesProjectFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("animes.zlx.com.br/exibir/\\d+/\\d+")
    private val HOST = "http://animes.zlx.com.br"
    private val TITLE_PATTERN = Pattern.compile("(?:(.*?)\\s:\\s)?(.*?)\\s(\\d+)")
    private val VIDEO_URL_PATTERN = Pattern.compile("[\"']([lmh]q)[\"'](?:.*?)src[\"']\\s?:\\s?[\"'](.*?)[\"']")
    private val QUALITY_ORDER = arrayOf("hq", "mq", "lq")

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.fetchUrl(url)
        val iframe = doc.select("#player_frame").attr("src")
        val videoUrl = fixLink(findLink(iframe))
        val title = doc.select(".serie-pagina-subheader span").text()
        val (animeName, description, number) = splitTitle(title)
        return Episode(animeName = animeName,
                description = "$description $number",
                number = number,
                video = videoUrl,
                link = url,
                nextEpisodes = nextEpisodes(doc))
    }

    private fun fixLink(link: String?): String? {
        if (link == null) return null
        return link.replace("\\/", "/")
    }

    private fun findLink(iframe: String?): String? {
        val html = UrlFetcher.fetchUrl(HOST + iframe).select("script").html()
        val videoMatcher = VIDEO_URL_PATTERN.matcher(html)
        val versions = HashMap<String, String>()
        while (videoMatcher.find())
            versions.put(videoMatcher.group(1), videoMatcher.group(2))
        for (quality in QUALITY_ORDER)
            if (versions.containsKey(quality))
                return versions[quality]
        return null
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