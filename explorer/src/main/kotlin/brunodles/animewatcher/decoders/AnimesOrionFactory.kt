package brunodles.animewatcher.decoders

import brunodles.animewatcher.AlchemistFactory
import brunodles.animewatcher.ToInt
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.urlfetcher.UrlFetcher
import brunodles.urlfetcher.href
import com.brunodles.alchemist.collectors.AttrCollector
import com.brunodles.alchemist.collectors.TextCollector
import com.brunodles.alchemist.selector.Selector
import org.jsoup.nodes.Document
import java.util.regex.Pattern

object AnimesOrionFactory : PageParser {

    @Suppress("RegExpRedundantEscape")
    private val URL_REGEX =
        Regex("^(?:https?\\:\\/\\/)?(?:www.)?animesorion.(\\w+)\\/(\\d+).*?\$")
    private val urlFetcher by lazy { UrlFetcher.fetcher() }

    override fun isEpisode(url: String): Boolean =
        url.matches(URL_REGEX)

    override fun episode(url: String): Episode {
        val document = urlFetcher.get(url)
        val html = document.html()
        if (document.title().contains("todos", true))
            return parseAbout(document, url)
        return parsePlayer(html, url)
    }

    private fun parseAbout(html: Document, url: String): Episode {
        val links = html.select(".lcp_catlist a")
        links.sortBy { it.attr("href").extractWithRegex("(\\d+)").toInt() }
        val first = links.removeAt(0).attr("href")
        val episode = parsePlayer(urlFetcher.get(first).html(), url)
        return episode.copy(nextEpisodes = links.map {
            Episode(
                it.text(), it.text().extractWithRegex("^(?:.*)\\s+?(\\d++)").toInt(),
                episode.animeName, link = it.href()
            )
        }.toList())
    }

    fun parsePlayer(html: String, url: String): Episode {
        val currentEpisode = AlchemistFactory.alchemist.parseHtml(html, CurrentEpisode::class.java)
        with(currentEpisode) {
            val nextEpisodeLink = nextEpisode()
            val nextEpisodes = if (URL_REGEX.matches(nextEpisodeLink))
                listOf(Episode("Next", number() + 1, animeName(), link = nextEpisodeLink))
            else emptyList()
            return Episode(
                description(), number(), animeName(), null, video(), url,
                nextEpisodes
            )
        }
    }

    interface CurrentEpisode {

        @Selector("[itemprop=name]")
        @AttrCollector("content")
        fun description(): String

        @Selector("[itemprop=description]")
        @AttrCollector("content")
        @Regexp("^(?:.*)\\s+?(\\d++)")
        @ToInt
        fun number(): Int

        @Selector("[rel='category tag']")
        @TextCollector
        fun animeName(): String?

        @Selector("video source")
        @AttrCollector("src")
        fun video(): String?

        @Selector(".controlesVideo a:last-child")
        @AttrCollector("href")
        fun nextEpisode(): String
    }
}

private fun String.extractWithRegex(s: String): String {
    val matcher = Pattern.compile(s).matcher(this)
    if (matcher.find())
        return matcher.group(1)
    return ""
}
