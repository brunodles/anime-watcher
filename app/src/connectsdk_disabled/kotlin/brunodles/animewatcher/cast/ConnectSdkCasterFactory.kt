package brunodles.animewatcher.castfalse

import android.app.Activity
import android.widget.ImageButton
import brunodles.animewatcher.explorer.Episode

object ConnectSdkCasterFactory {
    internal fun create(
        activity: Activity, mediaRouteButton: ImageButton?,
        listener: DeviceConnectedListener? = null
    ): Caster {
        return object : Caster {
            override fun playRemote(currentEpisode: Episode, position: Long) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun isConnected(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
}