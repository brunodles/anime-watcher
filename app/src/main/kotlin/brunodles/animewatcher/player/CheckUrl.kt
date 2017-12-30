package brunodles.animewatcher.player

import android.content.Intent
import android.util.Log
import brunodles.animacurse.AnimaCurseFactory
import brunodles.animesproject.AnimesProjectFactory
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParserFactory

object CheckUrl {

    val TAG = "CheckUrl"

    init {
        PageParserFactory.factories.add(AnimaCurseFactory)
        PageParserFactory.factories.add(AnimesProjectFactory)
    }

    fun findUrl(intent: Intent): String? {
        if (intent.data != null)
            return intent.data.toString()
        if (intent.hasExtra(Intent.EXTRA_TEXT))
            return intent.getStringExtra(Intent.EXTRA_TEXT)
        return null
    }

    fun videoInfo(url: String): Episode? {
        Log.d(TAG, "videoInfo $url\n Factories count = ${PageParserFactory.factories.size}")
        return PageParserFactory.factories.firstOrNull { it.isEpisode(url) }?.episode(url)
    }
}
