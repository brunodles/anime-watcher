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

    val connectSdk = ConnectSdkCaster(activity, imageButton) { current = it }.also { it.setOnEndListener(this::onEndListener) }
    val google = GoogleCaster(activity, mediaRouteButton) { current = it }.also { it.setOnEndListener(this::onEndListener) }
    var current: Caster = google
    var endListener: (() -> Unit)? = null

    override fun playRemote(currentEpisode: Episode, position: Long)
            = current.playRemote(currentEpisode, position)

    private fun onEndListener() {
        Log.d(TAG, "onEndListener: ")
        endListener?.invoke()
    }

    override fun setOnEndListener(listener: (() -> Unit)?) {
        endListener = listener
    }

    override fun isConnected(): Boolean = current.isConnected()
}