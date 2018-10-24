package brunodles.animewatcher.cast

import android.app.Activity
import android.widget.ImageButton

object ConnectSdkCasterFactory {
    internal fun create(
        activity: Activity, mediaRouteButton: ImageButton?,
        listener: DeviceConnectedListener? = null
    ): ConnectSdkCaster {
        return ConnectSdkCaster(activity, mediaRouteButton, listener)
    }
}