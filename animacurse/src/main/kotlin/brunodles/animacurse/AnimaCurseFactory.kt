package brunodles.animacurse

import bruno.animewatcher.explorer.AnimeFactory

object AnimaCurseFactory : AnimeFactory {

    private val URL_REGEX = Regex("animacurse\\.moe/?\\?p=")

    override fun isEpisode(url: String): Boolean =
            url.contains(URL_REGEX)

    override fun episode(url: String): AnimaCurseExplorer =
            AnimaCurseExplorer(url)

}