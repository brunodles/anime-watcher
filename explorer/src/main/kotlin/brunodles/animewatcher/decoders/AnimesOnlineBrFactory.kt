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

    private val EPISODE_URL_REGEX =
        Regex("https?://www\\.animesonlinebr\\.com\\.br/(?:video|desenho)/\\d+")

    override fun isEpisode(url: String): Boolean = url.matches(EPISODE_URL_REGEX)

    override fun episode(url: String): Episode {
        return if (url.contains("video"))
            decodeVideo(url)
        else
            decodeAbout(url)
    }

    private fun decodeVideo(url: String): Episode {
        val currentEpisode = AlchemistFactory.alchemist.parseUrl(url, CurrentEpisode::class.java)
        return with(currentEpisode) {
            Episode(
                description(),
                number(),
                animeName(),
                null,
                video(),
                url,
                nextEpisodes().toEpisode(animeName())
            )
        }
    }

    private fun decodeAbout(url: String): Episode {
        val about = AlchemistFactory.alchemist.parseUrl(url, About::class.java)
        val firstEpisodeUrl = about.nextEpisodes().removeAt(0).link()
        return decodeVideo(firstEpisodeUrl).copy(
            link = url,
            nextEpisodes = about.nextEpisodes().map {
                Episode(
                    number = it.number(),
                    description = it.description(),
                    animeName = about.animeName(),
                    link = it.link()
                )
            }
        )
    }

    interface About {

        @Selector(".boxAnimeSobreLinha [itemprop=author]")
        @TextCollector
        fun animeName(): String

        @Selector(".list li a")
        @Nested
        fun nextEpisodes(): ArrayList<EpisodeListItem>
    }

    interface EpisodeListItem {
        @Selector("a")
        @TextCollector
        fun description(): String

        @Selector("a")
        @AttrCollector("href")
        fun link(): String

        @Selector("a")
        @TextCollector
        @Regexp("^(?:.*)\\s+?(\\d++)")
        @ToInt
        fun number(): Int
    }

    interface CurrentEpisode {

        @Selector("[itemprop=description]")
        @TextCollector
        @Regexp("^(?:.*?[–-]\\s?)(.*?)\$")
        fun description(): String

        @Selector("[itemprop=description]")
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
