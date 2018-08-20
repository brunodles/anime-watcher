package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser

object UrlChecker {

    private val factories = ArrayList<PageParser>()

    init {
        factories.add(AnimaKaiFactory)
        factories.add(AnimesOnlineBrFactory)
        factories.add(AnimesOrionFactory)
        factories.add(AnimeTubeBrasilFactory)
        factories.add(AnitubeBrFactory)
        factories.add(AnitubeSiteFactory)
        factories.add(OnePieceXFactory) // this factory is causing stack overflow
        factories.add(TvCurseFactory)
        factories.add(XvideosFactory)
    }

    fun videoInfo(url: String): Episode? = factories.firstOrNull { it.isEpisode(url) }?.episode(url)
}
