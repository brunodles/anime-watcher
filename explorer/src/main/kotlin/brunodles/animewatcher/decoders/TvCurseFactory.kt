package brunodles.animewatcher.decoders

import brunodles.animewatcher.AlchemistFactory
import brunodles.animewatcher.ToInt
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import com.brunodles.alchemist.collectors.AttrCollector
import com.brunodles.alchemist.collectors.TextCollector
import com.brunodles.alchemist.nested.Nested
import com.brunodles.alchemist.regex.Regex
import com.brunodles.alchemist.selector.Selector

object TvCurseFactory : PageParser {
    private val URL_REGEX = kotlin.text.Regex("tvcurse\\.com/?\\?p=")

    override fun episode(url: String): Episode {
        val currentEpisode = AlchemistFactory.alchemist.parseUrl(url, CurrentEpisode::class.java)
        with(currentEpisode) {
            val nextEpisodes: List<Episode> = try {
                nextEpisodes().toEpisode(animeName())
            } catch (e: Exception) {
                emptyList()
            }
            val description = description()
            val number = number()
            return Episode(description, number, animeName(), image(), video(), url,
                    nextEpisodes)
        }
    }

    override fun isEpisode(url: String): Boolean =
            url.contains(URL_REGEX)

    private fun List<NextEpisode>?.toEpisode(animeName: String?): List<Episode> {
        this?.let {
            return it.map {
                with(it) {
                    Episode(description(), number(), animeName, image(), null, link())
                }
            }.toList()
        }
        return emptyList()
    }

    interface CurrentEpisode {

        @Selector("title")
        @TextCollector
        fun description(): String

        @Selector("title")
        @TextCollector
        @Regexp("^(?:.*)\\s+?(\\d++)")
        @ToInt
        fun number(): Int

        @Selector("[property=article:section]")
        @AttrCollector("content")
        fun animeName(): String?

        @Selector("[itemprop=thumbnailUrl]")
        @AttrCollector("content")
        fun image(): String?

        @Selector("video#video source")
        @AttrCollector("src")
        fun video(): String? = null

        @Selector(".episode a")
        @Nested
        fun nextEpisodes(): ArrayList<NextEpisode>?
    }

    interface NextEpisode {

        @Selector("#epnum")
        @TextCollector
        fun description(): String

        @Selector("#epnum")
        @TextCollector
        @Regex("(\\d+)")
        @ToInt
        fun number(): Int

        @Selector("#epimg img")
        @AttrCollector("src")
        fun image(): String?

        @Selector("a")
        @AttrCollector("href")
        fun link(): String
    }
}
