package brunodles.animewatcher.cast

import android.app.Activity
import android.support.v7.app.MediaRouteButton
import android.widget.ImageButton
import brunodles.animewatcher.explorer.Episode

internal class MultiCaster(activity: Activity, mediaRouteButton: MediaRouteButton, imageButton: ImageButton) :
        Caster {

    val connectSdk = ConnectSdkCaster(activity, imageButton) { current = it }.also { setOnEndListener(this::onEndListener) }
    val google = GoogleCaster(activity, mediaRouteButton) { current = it }.also { setOnEndListener(this::onEndListener) }
    var current: Caster = google
    var endListener: (() -> Unit)? = null

    override fun playRemote(currentEpisode: Episode, position: Long)
            = current.playRemote(currentEpisode, position)

    private fun onEndListener() {
        endListener?.invoke()
    }

    override fun setOnEndListener(listener: (() -> Unit)?) {
        endListener = listener
    }

}