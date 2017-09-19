package brunodles.animewatcher.player

import android.content.Intent
import android.util.Log
import brunodles.animewatcher.explorer.PageExplorer
import brunodles.animewatcher.explorer.FACTORIES

object CheckUrl {

    val TAG = "CheckUrl"

    fun findUrl(intent: Intent): String? {
        if (intent.data != null)
            return intent.data.toString()
        if (intent.hasExtra(Intent.EXTRA_TEXT))
            return intent.getStringExtra(Intent.EXTRA_TEXT)
        return null
    }

    fun videoInfo(url: String): PageExplorer? {
        Log.d(TAG, "videoInfo " + url)
        return FACTORIES.firstOrNull { it.isEpisode(url) }?.episode(url)
    }
}
