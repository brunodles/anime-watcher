package brunodles.animewatcher.decoders

import brunodles.animewatcher.AlchemistFactory
import brunodles.animewatcher.ToInt
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import com.brunodles.alchemist.collectors.AttrCollector
import com.brunodles.alchemist.collectors.TextCollector
import com.brunodles.alchemist.nested.Nested
import com.brunodles.alchemist.selector.Selector

object AnimesOnlineBrFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("https?://www\\.animesonlinebr\\.com\\.br/video/\\d+")

    override fun isEpisode(url: String): Boolean = url.matches(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val currentEpisode = AlchemistFactory.alchamist.parseUrl(url, CurrentEpisode::class.java)
        with(currentEpisode) {
            return Episode(description(), number(), animeName(), null, video(), url,
                    nextEpisodes().toEpisode(animeName()))
        }
    }

    interface CurrentEpisode {

        @Selector("[itemprop=description]")
        @TextCollector
        @Regexp("^(?:.*?[–-]\\s?)(.*?)\$")
        fun description(): String

        @Selector("[itemprop=name]")
        @TextCollector
        @Regexp("(\\d+)")
        @ToInt
        fun number(): Int

        @Selector("meta[property=\"article:section\"]")
        @AttrCollector("content")
        fun animeName(): String?

        @Selector("[itemprop=embedURL]")
        @AttrCollector("href")
        fun video(): String?

        @Selector(".setasVideo #seta02 a")
        @Nested
        fun nextEpisodes(): ArrayList<NextEpisode>?
    }

    interface NextEpisode {
        @Selector("a")
        @AttrCollector("href")
        fun link(): String

        @Selector("a")
        @AttrCollector("title")
        fun title(): String

        @Selector("a")
        @AttrCollector("title")
        @Regexp("(?:\\D|^)(\\d+)(?:\\D|\$)")
        @ToInt
        fun number(): Int
    }

    private fun List<NextEpisode>?.toEpisode(animeName: String?): List<Episode> {
        this?.let {
            return it.map {
                Episode(it.title(), it.number(), animeName, link = it.link())
            }.toList()
        }
        return emptyList()
    }

}
