package brunodles.animewatcher.decoders

import brunodles.animewatcher.AlchemistFactory
import brunodles.animewatcher.ToInt
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import com.brunodles.alchemist.collectors.AttrCollector
import com.brunodles.alchemist.collectors.TextCollector
import com.brunodles.alchemist.nested.Nested
import com.brunodles.alchemist.selector.Selector
import com.brunodles.alchemist.stringformat.StringFormat

object AnitubeBrFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("(?:https?://)?(?:www\\.)?anitubebr\\.com/vd/\\d+.*")

    override fun isEpisode(url: String): Boolean = url.matches(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val currentEpisode = AlchemistFactory.alchemist.parseUrl(url, CurrentEpisode::class.java)
        with(currentEpisode) {
            return Episode(description(), number(), animeName(), image(), null, url,
                    nextEpisodes()?.let {
                        listOf(Episode(it.title(), number() + 1, animeName(), link = it.link()))
                    })
        }
    }

    interface CurrentEpisode {

        @Selector("[itemprop=description]")
        @AttrCollector("content")
        fun description(): String

        @Selector("[itemprop=name]")
        @AttrCollector("content")
        @Regexp("(\\d+)")
        @ToInt
        fun number(): Int

        @Selector("[itemprop=video] [itemprop=name]")
        @AttrCollector("content")
        @Regexp("^([a-zA-Z\\s]+?)(?:[\\s\\d]+)\$")
        fun animeName(): String?

        @Selector("video source")
        @AttrCollector("src")
        fun video(): String?

        @Selector("[itemprop=thumbnailUrl]")
        @AttrCollector("content")
        fun image(): String?

        @Selector(".idonproximo a")
        @Nested
        fun nextEpisodes(): NextEpisode?
    }

    interface NextEpisode {
        @Selector("a")
        @AttrCollector("href")
        @StringFormat("https://anitubebr.com%s")
        fun link(): String

        @Selector("a")
        @TextCollector
        fun title(): String
    }
}
