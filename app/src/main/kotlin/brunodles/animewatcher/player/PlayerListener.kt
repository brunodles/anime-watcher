package brunodles.animewatcher.player

import android.util.Log
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray

internal class PlayerListener(val onPlayerStateChanged: (Int) -> Unit) : ExoPlayer.EventListener {

    companion object {
        val TAG = "PlayerListener"
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
        Log.d(TAG, "onPlaybackParametersChanged: $playbackParameters")
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
        Log.d(TAG, "onTracksChanged: ")
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        Log.d(TAG, "onPlayerError: $error")
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        Log.d(TAG, "onPlayerStateChanged: playWhenReady: $playWhenReady, playbackState: $playbackState")
        onPlayerStateChanged.invoke(playbackState)
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        Log.d(TAG, "onLoadingChanged: $isLoading")
    }

    override fun onPositionDiscontinuity() {
        Log.d(TAG, "onPositionDiscontinuity: ")
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
        Log.d(TAG, "onTimelineChanged: $timeline")
    }
}