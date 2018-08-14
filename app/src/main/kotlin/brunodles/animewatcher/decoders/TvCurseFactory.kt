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
import com.brunodles.alchemist.usevalueof.UseValueOf

object TvCurseFactory : PageParser {
    private val URL_REGEX = kotlin.text.Regex("tvcurse\\.com/?\\?p=")

    override fun episode(url: String): Episode {
        val currentEpisode = AlchemistFactory.alchamist.parseUrl(url, CurrentEpisode::class.java)
        with(currentEpisode) {
            return Episode(description(), number(), animeName(), imageUrl(number()), video(), url,
                    nextEpisodes().toEpisode(animeName()))
        }
    }

    fun imageUrl(episodeNumber: Int) =
            "https://tvcurse.com/imgs/one-piece-episodio-$episodeNumber.webp"

    override fun isEpisode(url: String): Boolean =
            url.contains(URL_REGEX)
}

private fun List<NextEpisode>?.toEpisode(animeName: String?): List<Episode>? {
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

    @Selector(".episodename")
    @TextCollector
    @Regex("^(?:.*?[–-]\\s?)(.*?)\$")
    fun description(): String

    @Selector(".episodename")
    @TextCollector
    @Regex("(?:\\D|^)(\\d+)(?:\\D|\$)")
    @ToInt
    fun number(): Int

    @Selector(".animename")
    @TextCollector
    fun animeName(): String? = null

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
    @Regex("^(?:.*?[–-]\\s?)(.*?)\$")
    fun description(): String

    @Selector("#epnum")
    @TextCollector
    @Regex("(?:\\D|^)(\\d+)(?:\\D|\$)")
    @ToInt
    fun number(): Int

    @Selector("img")
    @AttrCollector("src")
    fun image(): String?

    @Selector("a")
    @AttrCollector("href")
    fun link(): String
}