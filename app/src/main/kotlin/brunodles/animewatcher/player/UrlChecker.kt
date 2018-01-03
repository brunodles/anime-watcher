package brunodles.animewatcher.player

import android.content.Intent
import brunodles.animacurse.AnimaCurseFactory
import brunodles.animesorion.AnimesOrionFactory
import brunodles.animesproject.AnimesProjectFactory
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.anitubex.AnitubexFactory
import brunodles.animakai.AnimaKaiFactory
import brunodles.animesonlinebr.AnimesOnlineBrFactory
import brunodles.onepiecex.OnePieceXFactory

object UrlChecker {

    private val factories = ArrayList<PageParser>()

    init {
        factories.add(AnimaCurseFactory)
        factories.add(AnimaKaiFactory)
        factories.add(AnimesOnlineBrFactory)
        factories.add(AnimesOrionFactory)
        factories.add(AnimesProjectFactory)
        factories.add(AnitubexFactory)
        factories.add(OnePieceXFactory)
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
