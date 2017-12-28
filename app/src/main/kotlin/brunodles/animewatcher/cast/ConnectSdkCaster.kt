package brunodles.animewatcher.cast


import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageButton
import brunodles.animewatcher.R
import brunodles.animewatcher.explorer.Episode
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.ConnectableDeviceListener
import com.connectsdk.device.DevicePicker
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.service.DeviceService
import com.connectsdk.service.capability.MediaPlayer
import com.connectsdk.service.command.ServiceCommandError

internal class ConnectSdkCaster(val context: Activity, val mediaRouteButton: ImageButton?,
                                val listener: DeviceConnectedListener? = null) : Caster,
        ConnectableDeviceListener {

    companion object {
        val TAG = "GoogleCaster"
    }

    val mDiscoveryManager: DiscoveryManager
    var mDevice: ConnectableDevice? = null

    init {
        DiscoveryManager.init(context.applicationContext)
        // This step could even happen in your app's delegate
        mDiscoveryManager = DiscoveryManager.getInstance()
        mDiscoveryManager.start()

        mediaRouteButton?.setOnClickListener {
            pickDevice()
        }
    }

    private fun pickDevice() {
        val devicePicker = DevicePicker(context)
        val dialog = devicePicker.getPickerDialog("Cast to") { adapter: AdapterView<*>, parent: View, position: Int, id: Long ->
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
        Log.d(TAG, "playRemote: ")
        val mediaInfo = com.connectsdk.core.MediaInfo.Builder(currentEpisode.video!!, "video/mp4")
                .setTitle(currentEpisode.animeName ?: currentEpisode.description)
                .setDescription(currentEpisode.description)
                .setIcon(currentEpisode.image!!)
                .build()
        mDevice?.mediaPlayer?.playMedia(mediaInfo, false, object : MediaPlayer.LaunchListener {
            override fun onSuccess(result: MediaPlayer.MediaLaunchObject?) {
                Log.d(TAG, "playMedia.onSuccess: ")
                result?.mediaControl?.seek(position, null)
            }

            override fun onError(error: ServiceCommandError?) {
                Log.e(TAG, "playMedia.onError: ", error?.cause)
            }
        })
    }

}