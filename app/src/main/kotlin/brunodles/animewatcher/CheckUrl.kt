package brunodles.animewatcher

import android.content.Intent
import android.util.Log
import bruno.animewatcher.explorer.AnimeExplorer
import brunodles.animacurse.AnimaCurseFactory
import brunodles.animesproject.AnimesProjectFactory
import brunodles.anitubex.AnitubexFactory
import brunodles.onepiecex.OnePieceXFactory
import java.util.*

object CheckUrl {

    val TAG = "CheckUrl"
    val factories by lazy {
        Arrays.asList(
                AnitubexFactory, AnimaCurseFactory, AnimesProjectFactory, OnePieceXFactory
        )
    }

    fun findUrl(intent: Intent): String? {
        if (intent.data != null)
            return intent.data.toString()
        if (intent.hasExtra(Intent.EXTRA_TEXT))
            return intent.getStringExtra(Intent.EXTRA_TEXT)
        return null
    }

    fun videoInfo(url: String): AnimeExplorer? {
        Log.d(TAG, "videoInfo " + url)
        return factories.firstOrNull { it.isEpisode(url) }?.episode(url)
    }
}
