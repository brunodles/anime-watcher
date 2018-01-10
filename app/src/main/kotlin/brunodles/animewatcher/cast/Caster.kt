package brunodles.animewatcher.cast

import android.app.Activity
import android.support.v7.app.MediaRouteButton
import android.widget.ImageButton
import brunodles.animewatcher.explorer.Episode

interface Caster {

    fun playRemote(currentEpisode: Episode, position: Long)
    fun setOnEndListener(listener: (() -> Unit)? = null)
    fun isConnected(): Boolean

    object Factory {
        fun connectSdkCaster(activity: Activity, button: ImageButton): Caster
                = ConnectSdkCaster(activity, button)

        fun googleCaster(activity: Activity, mediaRouterButton: MediaRouteButton): Caster
                = GoogleCaster(activity, mediaRouterButton)

        fun multiCaster(activity: Activity, mediaRouterButton: MediaRouteButton, imageButton: ImageButton): Caster
                = MultiCaster(activity, mediaRouterButton, imageButton)
    }
}

fun Caster?.isConnected(): Boolean = this?.isConnected() ?: false