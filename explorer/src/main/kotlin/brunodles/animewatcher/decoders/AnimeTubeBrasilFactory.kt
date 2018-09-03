package brunodles.animewatcher.decoders

import brunodles.animewatcher.AlchemistFactory
import brunodles.animewatcher.ToInt
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import com.brunodles.alchemist.collectors.AttrCollector
import com.brunodles.alchemist.collectors.TextCollector
import com.brunodles.alchemist.selector.Selector
import com.brunodles.alchemist.stringformat.StringFormat

object AnimeTubeBrasilFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("(?:https?://)?(?:www\\.)?animetubebrasil\\.com/\\d+.*")

    override fun isEpisode(url: String): Boolean = url.matches(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val currentEpisode = AlchemistFactory.alchemist.parseUrl(url, CurrentEpisode::class.java)
        with(currentEpisode) {
            return Episode(description(), number(), animeName(), null, video(), url,
                    listOf(Episode("Next", number() + 1, animeName(),
                            link = nextEpisode()))
            )
        }
    }

    interface CurrentEpisode {

        @Selector(".epTituloNn")
        @TextCollector
        @Regexp("^(?:.*?):\\s+?(.*)\$")
        fun description(): String

        @Selector(".epTituloNn")
        @TextCollector
        @Regexp("(\\d+)")
        @ToInt
        fun number(): Int

        @Selector(".epTituloTit")
        @TextCollector
        fun animeName(): String?

        @Selector("video source")
        @AttrCollector("src")
        fun video(): String?

        @Selector(".epVideoControl .epVideoControlItem:last-child a")
        @AttrCollector("href")
        fun nextEpisode(): String
    }
}
