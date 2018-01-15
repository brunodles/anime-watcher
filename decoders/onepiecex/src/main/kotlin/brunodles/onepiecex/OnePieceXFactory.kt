package brunodles.onepiecex

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.urlfetcher.UrlFetcher
import brunodles.urlfetcher.src
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object OnePieceXFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("one-piece-x.com.br/episodios/online/\\d+")
    private val SERVER_URL_PATTERN = Pattern.compile("var\\s+servidor\\s*=\\s*[\"'](.*?)[\"'];")
    private val DATA_URL_PATTERN = Pattern.compile("var\\s*codigo\\s*=\\s*[\"'](https?)://[\"'].*?[\"'](.*?)[\"'];")
    private val VIDEO_URL_PATTERN = Pattern.compile("[\"']([HML]Q|Online)[\"']:\\s*[\"'](.*?)[\"']")
    private val EPISODE_NUMBER_PATTERN = Pattern.compile("\\d+")
    private val HOST = "https://one-piece-x.com.br"
    private val PREFERED_QUALITY_ORDER = arrayOf("HQ", "MQ", "LQ", "ONLINE")
    var printEpisodes: Boolean = false

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.composableFetcher(url).get()

        val text = doc.select("option[selected]").text()
        val iframeLink = doc.select("#bannerVideoOnline iframe").src()
        val number = extractNumberFrom(text)
        val episodes = findNextEpisodes(doc)
                .filter { it.number > number }

        if (printEpisodes)
            episodes.forEach { episode ->
                println("Episode(description = \"${episode.description}\",\n" +
                        "\tnumber = ${episode.number},\n" +
                        "\tanimeName = \"One Piece\",\n" +
                        "\tlink = \"${episode.link}\"),")
            }

        return Episode(description = text.split(":").last().trim(),
                number = number,
                animeName = "One Piece",
                image = null,
                video = findVideoUrl(iframeLink),
                link = url,
                nextEpisodes = episodes
        )
    }

    private fun extractNumberFrom(text: String?): Int {
        val matcher = EPISODE_NUMBER_PATTERN.matcher(text)
        if (matcher.find())
            return matcher.group(0).toInt()
        return 0
    }

    private fun findVideoUrl(iframeLink: String): String? {
        val iFrameHtml = UrlFetcher.composableFetcher(iframeLink).get().html()
        val server = findServer(iFrameHtml)
        val jsonText = findData(iFrameHtml, server)

        val matcher = VIDEO_URL_PATTERN.matcher(jsonText)
        val videos = HashMap<String, String>()
        while (matcher.find())
            videos.put(matcher.group(1).toUpperCase(), matcher.group(2))

        for (quality in PREFERED_QUALITY_ORDER)
            if (videos.containsKey(quality))
                return videos[quality]!!.replace("\\/", "/")
        return null
    }

    private fun findServer(iframeHtml: String): String {
        val matcher = SERVER_URL_PATTERN.matcher(iframeHtml)
        if (!matcher.find())
            return "yasopp"
        return matcher.group(1)
    }

    private fun findData(iframeHtml: String, server: String): String {
        val matcher = DATA_URL_PATTERN.matcher(iframeHtml)
        matcher.find() // should we throw and exception?
        val jsonUrl = "${matcher.group(1)}://$server${matcher.group(2)}"
        return UrlFetcher.composableFetcher(jsonUrl).get().text()
    }

    private fun findNextEpisodes(doc: Document): List<Episode> {
        return doc.select("select#episodiosLista option").map {
            val text = it.text()
            val link = HOST + it.attr("value")
            Episode(description = text.split(":").last().trim(),
                    number = extractNumberFrom(text),
                    animeName = "One Piece",
                    link = link)
        }.toList()
    }
}
