package brunodles.animewatcher.cast


import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import brunodles.animewatcher.R
import brunodles.animewatcher.explorer.Episode
import com.connectsdk.core.MediaInfo
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.ConnectableDeviceListener
import com.connectsdk.device.DevicePicker
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.service.DeviceService
import com.connectsdk.service.capability.MediaControl
import com.connectsdk.service.capability.MediaPlayer
import com.connectsdk.service.command.ServiceCommandError

@Suppress("DEPRECATION")
internal class ConnectSdkCaster(activity: Activity, val mediaRouteButton: ImageButton?,
                                val listener: DeviceConnectedListener? = null) : Caster,
        ConnectableDeviceListener {

    companion object {
        val TAG = "ConnectSdkCaster"
    }

    val mDiscoveryManager: DiscoveryManager
    var mDevice: ConnectableDevice? = null
    private var endListener: (() -> Unit)? = null

    init {
        DiscoveryManager.init(activity.applicationContext)
        // This step could even happen in your app's delegate
        mDiscoveryManager = DiscoveryManager.getInstance()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            mDiscoveryManager.start()
        mediaRouteButton?.setOnClickListener { pickDevice(activity) }
    }

    private fun pickDevice(activity: Activity) {
        val devicePicker = DevicePicker(activity)
        val dialog = devicePicker.getPickerDialog("Cast to") { adapter: AdapterView<*>, _: View, position: Int, _: Long ->
            mDevice = adapter.getItemAtPosition(position) as ConnectableDevice
            mDevice?.let {
                it.addListener(this)
                it.connect()
            }
        }
        dialog.show()
    }

    override fun onDeviceDisconnected(device: ConnectableDevice?) {
        Log.d(TAG, "onDeviceDisconnected: ")
        mediaRouteButton?.setImageResource(R.drawable.ic_cast_black_24dp)
    }

    override fun onDeviceReady(device: ConnectableDevice?) {
        Log.d(TAG, "onDeviceReady: ${device?.friendlyName}")
        mediaRouteButton?.setImageResource(R.drawable.ic_cast_connected_black_24dp)
        listener?.invoke(this)
    }

    override fun onPairingRequired(device: ConnectableDevice?, service: DeviceService?, pairingType: DeviceService.PairingType?) {
        Log.d(TAG, "onPairingRequired: ")
    }

    override fun onCapabilityUpdated(device: ConnectableDevice?, added: MutableList<String>?, removed: MutableList<String>?) {
        Log.d(TAG, "onCapabilityUpdated: ")
    }

    override fun onConnectionFailed(device: ConnectableDevice?, error: ServiceCommandError?) {
        Log.e(TAG, "onConnectionFailed: ", error?.cause)
    }


    override fun playRemote(currentEpisode: Episode, position: Long) {
        Log.d(TAG, "playRemote: ${currentEpisode}")
        val mediaInfo = MediaInfo.Builder(currentEpisode.video!!, "video/mp4")
                .setTitle(currentEpisode.animeName ?: currentEpisode.description)
                .setDescription(currentEpisode.description)
                .setIcon(currentEpisode.image!!)
                .build()
        val launchListener = SeekOnLaunchListener(position) { endListener?.invoke() }
        mDevice?.mediaPlayer?.playMedia(mediaInfo, false, launchListener)
    }

    private class SeekOnLaunchListener(val position: Long, val endListener: () -> Unit) :
            MediaPlayer.LaunchListener {
        override fun onSuccess(result: MediaPlayer.MediaLaunchObject?) {
            Log.d(TAG, "SeekOnLaunchListener.onSuccess: ")
            result?.mediaControl?.let {
                it.seek(position, null)
                it.subscribePlayState(object : MediaControl.PlayStateListener {
                    override fun onSuccess(status: MediaControl.PlayStateStatus?) {
                        Log.d(TAG, "PlayStateListener.onSuccess: state: \"${status?.name}\"")
                        if (status == MediaControl.PlayStateStatus.Finished) {
                            endListener.invoke()
                        }
                    }

                    override fun onError(error: ServiceCommandError?) {
                        Log.e(TAG, "PlayStateListener.onError: ", error?.cause)
                    }

                })
            }
        }

        override fun onError(error: ServiceCommandError?) {
            Log.e(TAG, "SeekOnLaunchListener.onError: ", error?.cause)
        }
    }

    override fun setOnEndListener(listener: (() -> Unit)?) {
        Log.d(TAG, "setOnEndListener: ")
        this.endListener = listener
    }

    override fun isConnected(): Boolean = mDevice?.isConnected ?: false
}