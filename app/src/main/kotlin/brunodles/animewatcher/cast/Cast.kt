package brunodles.animewatcher.cast

import android.content.Context
import android.net.Uri
import android.support.v7.app.MediaRouteButton
import android.util.Log
import brunodles.animewatcher.explorer.Episode
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.common.images.WebImage

class Cast(val context: Context, mediaRouteButton: MediaRouteButton?) {

    companion object {
        val TAG = "Cast"
    }

    val mSessionManager: SessionManager

    //    val mSessionManagerListener: SessionManagerListener = SessionManagerListenerImpl()
    init {
        val castContext = CastContext.getSharedInstance(context)
        mSessionManager = castContext.sessionManager
        CastButtonFactory.setUpMediaRouteButton(context, mediaRouteButton)
        mSessionManager.currentCastSession
    }

    val mCastSession: CastSession by lazy { mSessionManager.currentCastSession }

    fun onResume() {
        //        mCastSession = mSessionManager.currentCastSession
    }

    fun onPause() {
        // todo clean cast session on app pause.
        //        mCastSession = null
    }

    fun playRemote(currentEpisode: Episode, position: Long) {
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
        val remoteMediaClient = mCastSession.remoteMediaClient
        val mediaLoadOptions = MediaLoadOptions.Builder()
                .setAutoplay(true)
                .setPlayPosition(position)
                .build()
        remoteMediaClient?.load(mediaInfo, mediaLoadOptions)
        remoteMediaClient?.play()
    }
}