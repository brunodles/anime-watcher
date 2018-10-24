package brunodles.animewatcher.cast

import android.app.Activity
import android.support.v7.app.MediaRouteButton
import android.util.Log
import android.widget.ImageButton
import brunodles.animewatcher.explorer.Episode

internal class MultiCaster(activity: Activity, mediaRouteButton: MediaRouteButton, imageButton: ImageButton) :
        Caster {

    companion object {
        val TAG = "MultiCaster"
    }

    val connectSdk = ConnectSdkCaster(activity, imageButton) { current = it }
    val google = GoogleCaster(activity, mediaRouteButton) { current = it }
    var current: Caster = google

    override fun playRemote(currentEpisode: Episode, position: Long)
            = current.playRemote(currentEpisode, position)

    override fun isConnected(): Boolean = current.isConnected()
}