package brunodles.animewatcher.decoders

import brunodles.animewatcher.AlchemistFactory
import brunodles.animewatcher.ToInt
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.urlfetcher.UrlFetcher
import com.brunodles.alchemist.collectors.AttrCollector
import com.brunodles.alchemist.collectors.TextCollector
import com.brunodles.alchemist.navigate.Navigate
import com.brunodles.alchemist.selector.Selector
import com.brunodles.alchemist.stringformat.StringFormat
import org.jsoup.nodes.Element
import java.util.regex.Pattern

object OnePieceXFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("one-?piece-e?x.com.br/episodios/online/\\d+")
    private val SERVER_URL_PATTERN = Pattern.compile("var\\s+servidor\\s*=\\s*[\"'](.*?)[\"'];")
    private val DATA_URL_PATTERN =
        Pattern.compile("var\\s*codigo\\s*=\\s*[\"'](https?):\\/\\/[\"'].*?[\"'](.*?)[\"'];")
    private val VIDEO_URL_PATTERN = Pattern.compile("[\"']([HML]Q|Online)[\"']:\\s*[\"'](.*?)[\"']")
    private val PREFERED_QUALITY_ORDER = arrayOf("HQ", "MQ", "LQ", "ONLINE")
    private const val ANIME_NAME = "One Piece"
    private val urlFetcher by lazy { UrlFetcher.fetcher() }

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val episode = AlchemistFactory.alchemist.parseUrl(url, CurrentEpisode::class.java)
        with(episode) {
            val videoUrl = findVideoUrl(video().html())

            return Episode(
                description(), number(), ANIME_NAME, null, videoUrl,
                url, listOf(
                    Episode(
                        "Next", number() + 1, ANIME_NAME,
                        link = nextEpisode()
                    )
                )
            )
        }
    }

    private fun findVideoUrl(iFrameHtml: String): String? {
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
        return urlFetcher.get(jsonUrl).text()
    }

    interface CurrentEpisode {

        @Selector(".info h2")
        @TextCollector
        fun description(): String

        @Selector(".info h1")
        @TextCollector
        @Regexp("(?:\\D|^)(\\d+)(?:\\D|\$)")
        @ToInt
        fun number(): Int

        @Selector("#player")
        @AttrCollector("src")
        @Navigate
        fun video(): Element

        @Selector("a#proximo-player")
        @AttrCollector("href")
        @StringFormat("https://onepiece-ex.com.br%s")
        fun nextEpisode(): String
    }
}
