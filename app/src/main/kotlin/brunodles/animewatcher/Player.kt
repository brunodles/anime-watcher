package brunodles.animewatcher

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class Player(val context: Context, val playerView: SimpleExoPlayerView?) {

    companion object {
        val USER_AGENT = "AnimeWatcher"
    }

    private var exoPlayer: SimpleExoPlayer? = null

    init {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        playerView?.player = exoPlayer
    }

    fun prepareVideo(url: String) {
        val bandwidthMeter = DefaultBandwidthMeter()
        val dataSourceFactory = DefaultDataSourceFactory(context,
                Util.getUserAgent(context, USER_AGENT), bandwidthMeter)
        val extractorsFactory = DefaultExtractorsFactory()
        val videoSource = ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null)
        exoPlayer?.prepare(videoSource, true, true)
        exoPlayer?.playWhenReady
    }

    fun getCurrentPosition() = exoPlayer?.currentPosition ?: 0

    fun stop() = exoPlayer?.stop()
}