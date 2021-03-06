package brunodles.animewatcher.cast

import android.app.Activity
import android.support.v7.app.MediaRouteButton
import android.widget.ImageButton
import brunodles.animewatcher.explorer.Episode

interface Caster {

    fun playRemote(currentEpisode: Episode, position: Long)
    fun isConnected(): Boolean

    object Factory {
        fun multiCaster(activity: Activity, mediaRouterButton: MediaRouteButton, imageButton: ImageButton): Caster
                = MultiCaster(activity, mediaRouterButton, imageButton)
    }
}

fun Caster?.isConnected(): Boolean = this?.isConnected() ?: false