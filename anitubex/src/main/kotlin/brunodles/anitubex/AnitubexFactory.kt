package brunodles.anitubex

import bruno.animewatcher.explorer.AnimeExplorer
import bruno.animewatcher.explorer.AnimeFactory

object AnitubexFactory : AnimeFactory {

    private val EPISODE_URL_REGEX = Regex("anitubex.com/.*?\\d+")

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): AnimeExplorer =
            AnitubexExplorer(url)
}