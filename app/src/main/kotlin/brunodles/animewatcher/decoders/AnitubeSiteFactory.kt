package brunodles.animewatcher.decoders

import brunodles.animewatcher.AlchemistFactory
import brunodles.animewatcher.ToInt
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import com.brunodles.alchemist.collectors.AttrCollector
import com.brunodles.alchemist.collectors.TextCollector
import com.brunodles.alchemist.nested.Nested
import com.brunodles.alchemist.selector.Selector

object AnitubeSiteFactory : PageParser {

    private val EPISODE_URL_REGEX = Regex("https?://www\\.anitube\\.site/\\d+.*")

    override fun isEpisode(url: String): Boolean = url.matches(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        val currentEpisode = AlchemistFactory.alchamist.parseUrl(url, CurrentEpisode::class.java)
        with(currentEpisode) {
            return Episode(description(), number(), animeName(), image(), video(), url,
                    nextEpisodes()?.let {
                        listOf(Episode(it.title(), it.number(), animeName(), link = it.link()))
                    })
        }
    }

    interface CurrentEpisode {

        @Selector("#descricao p")
        @TextCollector
        @Regexp("^(?:.*?[–-]+\\s?)+(.*?)\$")
        fun description(): String

        @Selector("#descricao p")
        @TextCollector
        @Regexp("(\\d+)")
        @ToInt
        fun number(): Int

        @Selector("title")
        @TextCollector
        @Regexp("^([\\w\\s]+)[\\s-]")
        fun animeName(): String?

        @Selector("video source")
        @AttrCollector("src")
        fun video(): String?

        @Selector("video")
        @AttrCollector("poster")
        fun image(): String?

        @Selector("#baixo #right a")
        @Nested
        fun nextEpisodes(): NextEpisode?
    }

    interface NextEpisode {
        @Selector("a")
        @AttrCollector("href")
        fun link(): String

        @Selector("a")
        @AttrCollector("title")
        @Regexp("^(?:.*?[–-]+\\s?)+(.*?)\$")
        fun title(): String

        @Selector("a")
        @AttrCollector("title")
        @Regexp("(\\d+)")
        @ToInt
        fun number(): Int
    }
}
