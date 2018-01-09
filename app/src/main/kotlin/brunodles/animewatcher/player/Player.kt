package brunodles.animewatcher.player

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

class Player(context: Context, playerView: SimpleExoPlayerView) {

    companion object {
        val TAG = "Player"
        val USER_AGENT = "AnimeWatcher"
    }

    private val exoPlayer: SimpleExoPlayer

    private val userAgent: String by lazy { Util.getUserAgent(context, USER_AGENT) }

    init {
        Log.d(TAG, "init: ")
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        playerView.player = exoPlayer
    }

    fun prepareVideo(url: String) {
        Log.d(TAG, "prepareVideo: $url")
        val bandwidthMeter = DefaultBandwidthMeter()
        val dataSourceFactory = DefaultHttpDataSourceFactory(userAgent, bandwidthMeter,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true)
        val extractorsFactory = DefaultExtractorsFactory()
        val videoSource = ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null)
        exoPlayer.prepare(videoSource, false, false)
        exoPlayer.playWhenReady = true
    }

    fun getCurrentPosition() = exoPlayer.currentPosition

    fun stopAndRelease() {
        Log.d(TAG, "stopAndRelease: ")
        exoPlayer.stop()
        exoPlayer.release()
    }

    fun seekTo(position: Long) {
        Log.d(TAG, "seekTo: $position")
        exoPlayer.seekTo(position)
        exoPlayer.playWhenReady = true
        exoPlayer.playbackState
    }
}