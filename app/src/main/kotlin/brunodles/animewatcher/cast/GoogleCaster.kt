package brunodles.animewatcher.cast

import android.content.Context
import android.net.Uri
import android.support.v7.app.MediaRouteButton
import android.util.Log
import brunodles.animewatcher.explorer.Episode
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.framework.*
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage

class GoogleCaster(context: Context, mediaRouteButton: MediaRouteButton?,
                   val listener: DeviceConnectedListener? = null) : Caster {

    companion object {
        val TAG = "GoogleCaster"
    }

    val mSessionManager: SessionManager

    //    val mSessionManagerListener: SessionManagerListener = SessionManagerListenerImpl()
    init {
        val applicationContext = context.applicationContext
        val castContext = CastContext.getSharedInstance(applicationContext)
        mSessionManager = castContext.sessionManager
        CastButtonFactory.setUpMediaRouteButton(applicationContext, mediaRouteButton)
        castContext.addCastStateListener { if (it == CastState.CONNECTED) listener?.invoke(this) }
    }

    fun castSession(): CastSession? = mSessionManager.currentCastSession

    override fun playRemote(currentEpisode: Episode, position: Long) {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)

        movieMetadata.putString(MediaMetadata.KEY_TITLE, currentEpisode.description)
        //            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, mSelectedMedia.getStudio());
        currentEpisode.image?.let { movieMetadata.addImage(WebImage(Uri.parse(it))) }

        Log.d(TAG, "onCreate: currentEpisode.video = ${currentEpisode.video}")
        val mediaInfo = MediaInfo.Builder(currentEpisode.video)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMetadata(movieMetadata)
                //                    .setStreamDuration(mSelectedMedia.getDuration() * 1000)
                .build()
        val remoteMediaClient = castSession()?.remoteMediaClient
        val mediaLoadOptions = MediaLoadOptions.Builder()
                .setAutoplay(true)
                .setPlayPosition(position)
                .build()
        remoteMediaClient?.load(mediaInfo, mediaLoadOptions)
        remoteMediaClient?.play()
    }

    override fun isConnected(): Boolean = castSession()?.isConnected ?: false
}