package brunodles.animewatcher

import android.content.Context
import android.support.v7.app.MediaRouteButton
import android.util.Log
import brunodles.animewatcher.explorer.CurrentEpisode
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager

class Cast(val context: Context, mediaRouteButton: MediaRouteButton?) {

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

    fun playRemove(currentEpisode: CurrentEpisode?, position: Long) {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)

        movieMetadata.putString(MediaMetadata.KEY_TITLE, currentEpisode?.description)
//            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, mSelectedMedia.getStudio());
//            movieMetadata.addImage(WebImage(Uri.parse(currentEpisode?.)));
//            movieMetadata.addImage(WebImage(Uri.parse(mSelectedMedia.getImage(1))));

        Log.d(MainActivity.TAG, "onCreate: currentEpisode.video = ${currentEpisode?.video}")
        val mediaInfo = MediaInfo.Builder(currentEpisode?.video)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMetadata(movieMetadata)
//                    .setStreamDuration(mSelectedMedia.getDuration() * 1000)
                .build()
        val remoteMediaClient = mCastSession.remoteMediaClient
        remoteMediaClient?.load(mediaInfo, true, position)
        remoteMediaClient?.play()
    }
}