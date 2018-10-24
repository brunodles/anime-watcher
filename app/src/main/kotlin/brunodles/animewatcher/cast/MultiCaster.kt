package brunodles.animewatcher.cast

import android.app.Activity
import android.support.v7.app.MediaRouteButton
import android.widget.ImageButton
import brunodles.animewatcher.BuildConfig
import brunodles.animewatcher.explorer.Episode

internal class MultiCaster(
    activity: Activity,
    mediaRouteButton: MediaRouteButton,
    imageButton: ImageButton
) :
    Caster {

    companion object {
        val TAG = "MultiCaster"
    }

    init {
        if (BuildConfig.CONNECT_SDK)
            addCaster(ConnectSdkCasterFactory.create(activity, imageButton) { current = it })
        addCaster(GoogleCaster(activity, mediaRouteButton) { current = it })
    }

    val casters = ArrayList<Caster>()
    var current: Caster? = null

    private fun addCaster(caster: Caster) {
        casters.add(caster)
        current = caster
    }

    override fun playRemote(currentEpisode: Episode, position: Long) {
        current?.playRemote(currentEpisode, position)
    }

    override fun isConnected(): Boolean = current?.isConnected() ?: false
}