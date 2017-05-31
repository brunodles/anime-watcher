package brunodles.animewatcher

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import bruno.animewatcher.explorer.AnimeExplorer
import brunodles.animacurse.AnimaCurseFactory
import brunodles.animesproject.AnimesProjectFactory
import brunodles.animewatcher.databinding.ActivityMainBinding
import brunodles.anitubex.AnitubexFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
        val USER_AGENT = "AnimeWatcher"
    }

    var binding: ActivityMainBinding? = null
    var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createPlayer();
    }

    private fun createPlayer() {
        // 1. Create a default TrackSelector
        val mainHandler = Handler()
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // 2. Create the player
        val player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        binding?.player?.player = player
    }

    override fun onResume() {
        super.onResume()
        CheckUrl(intent) {
            val currentEpisode = it.currentEpisode()
            prepareVideo(currentEpisode.video)
            binding?.title?.text = currentEpisode.description

//            binding?.text?.text = it
            // share to another player
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.setData(Uri.parse(it))
//            startActivity(intent)
        }.execute()
    }

    fun prepareVideo(url: String) {
        // Measures bandwidth during playback. Can be null if not required.
        val bandwidthMeter = DefaultBandwidthMeter()
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, USER_AGENT), bandwidthMeter)
        // Produces Extractor instances for parsing the media data.
        val extractorsFactory = DefaultExtractorsFactory()
        // This is the MediaSource representing the media to be played.
        val videoSource = ExtractorMediaSource(Uri.parse(url),
                dataSourceFactory, extractorsFactory, null, null)
        // Prepare the player with the source.
        player?.prepare(videoSource)
    }

    private class CheckUrl(private val intent: Intent, private val function: (AnimeExplorer) -> Unit) :
            AsyncTask<Void, Void, AnimeExplorer?>() {

        val factories by lazy { Arrays.asList(AnitubexFactory, AnimaCurseFactory, AnimesProjectFactory) }

        override fun doInBackground(vararg params: Void?): AnimeExplorer? {
            val url = findUrl()
            if (url == null) {
                this.cancel(true)
                return null
            }
            return findVideoUrl(url)
        }

        fun findUrl(): String? {
            if (intent.data != null)
                return intent.data.toString()
            else if (intent.hasExtra(Intent.EXTRA_TEXT))
                return intent.getStringExtra(Intent.EXTRA_TEXT)
            return null
        }


        fun findVideoUrl(url: String): AnimeExplorer? {
            Log.d(TAG, "findVideoUrl " + url)
            for (factory in factories) {
                if (factory.isEpisode(url))
                    return factory.episode(url)
            }
            return null
        }

        override fun onPostExecute(result: AnimeExplorer?) {
            super.onPostExecute(result)
            Log.d(TAG, "onPostExecute " + result)
            if (result != null)
                function(result)
        }
    }
}
