package brunodles.animewatcher.decoders

import android.content.Intent
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser

object UrlChecker {

    private val factories = ArrayList<PageParser>()

    init {
        factories.add(AnimaKaiFactory)
        factories.add(AnimesOnlineBrFactory)
        factories.add(AnimesOrionFactory)
        factories.add(OnePieceXFactory)
        factories.add(XvideosFactory)
        factories.add(TvCurseFactory)
    }

    fun findUrl(intent: Intent): String? {
        if (intent.data != null)
            return intent.data.toString()
        if (intent.hasExtra(Intent.EXTRA_TEXT))
            return intent.getStringExtra(Intent.EXTRA_TEXT)
        return null
    }

    fun videoInfo(url: String): Episode? = factories.firstOrNull { it.isEpisode(url) }?.episode(url)
}
