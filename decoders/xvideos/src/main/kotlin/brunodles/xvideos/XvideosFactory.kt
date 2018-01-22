package brunodles.animacurse

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.xvideos.XvideoExplorer

object XvideosFactory : PageParser {

    private val URL_REGEX = Regex("xvideos.com/.*?\\d+")

    override fun isEpisode(url: String): Boolean = url.contains(URL_REGEX)

    override fun episode(url: String): Episode =
            XvideoExplorer(url).currentVideo()

}

