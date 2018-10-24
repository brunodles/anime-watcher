package brunodles.animewatcher.cast

import android.app.Activity
import android.support.v7.app.MediaRouteButton
import android.util.Log
import android.widget.ImageButton
import brunodles.animewatcher.BuildConfig
import brunodles.animewatcher.explorer.Episode

internal class MultiCaster(activity: Activity, mediaRouteButton: MediaRouteButton, imageButton: ImageButton) :
        Caster {

    val casters = ArrayList<Caster>()
    var current: Caster? = null
    var endListener: (() -> Unit)? = null

    companion object {
        val TAG = "MultiCaster"
    }

    init {
        if (BuildConfig.CONNECT_SDK)
            addCaster(ConnectSdkCasterFactory.create(activity, imageButton) { current = it }.also { it.setOnEndListener(this::onEndListener) })
        addCaster(GoogleCaster(activity, mediaRouteButton) { current = it }.also { it.setOnEndListener(this::onEndListener) })
    }

    private fun addCaster(caster : Caster) {
        casters.add(caster)
        current = caster
    }

    override fun playRemote(currentEpisode: Episode, position: Long) {
         current?.playRemote(currentEpisode, position)
    }

    private fun onEndListener() {
        Log.d(TAG, "onEndListener: ")
        endListener?.invoke()
    }

    override fun setOnEndListener(listener: (() -> Unit)?) {
        endListener = listener
    }

    override fun isConnected(): Boolean = current?.isConnected() ?: false
}