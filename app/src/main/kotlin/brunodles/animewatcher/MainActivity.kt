package brunodles.animewatcher

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.ImageView
import bruno.animewatcher.explorer.AnimeExplorer
import bruno.animewatcher.explorer.EpisodeLink
import brunodles.animacurse.AnimaCurseFactory
import brunodles.animesproject.AnimesProjectFactory
import brunodles.animewatcher.databinding.ActivityMainBinding
import brunodles.animewatcher.databinding.ItemEpisodeBinding
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
import com.google.android.gms.cast.framework.CastContext
import com.squareup.picasso.Picasso
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
        val USER_AGENT = "AnimeWatcher"
    }

    var binding: ActivityMainBinding? = null
    var player: SimpleExoPlayer? = null
    var adapter: GenericAdapter<EpisodeLink, ItemEpisodeBinding>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createPlayer()
        setupRecyclerView()

        val castContext = CastContext.getSharedInstance(this)
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.nextEpisodes?.layoutManager = manager
        adapter = GenericAdapter<EpisodeLink, ItemEpisodeBinding>(R.layout.item_episode) { viewHolder, item, index ->
            viewHolder.binder.description.text = item.description
            loadImageInto(item.image, viewHolder.binder.image)
        }
        binding?.nextEpisodes?.adapter = adapter
    }

    private fun loadImageInto(url: String?, image: ImageView) {
        if (url == null) return
        Picasso.Builder(this).indicatorsEnabled(true)
                .loggingEnabled(true)
                .build()
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(image)
    }

    private fun createPlayer() {
        val mainHandler = Handler()
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        binding?.player?.player = player
    }

    override fun onResume() {
        super.onResume()
        CheckUrl(intent, this) {
            val currentEpisode = it.currentEpisode()
            prepareVideo(currentEpisode.video)
            binding?.title?.text = currentEpisode.description

            adapter?.list = it.nextEpisodes()

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
        player?.prepare(videoSource, true, true)
        player?.playWhenReady
    }

    private class CheckUrl(private val intent: Intent, private val context: Context, private val function: (AnimeExplorer) -> Unit) :
            AsyncTask<Void, Void, AnimeExplorer?>() {

        val factories by lazy { Arrays.asList(AnitubexFactory, AnimaCurseFactory, AnimesProjectFactory) }
        val preference by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

        override fun doInBackground(vararg params: Void?): AnimeExplorer? {
            val url = findUrl()
            if (url == null) {
                this.cancel(true)
                return null
            }
            preference.edit()
                    .putString("URL", url)
                    .apply()
            return findVideoUrl(url)
        }

        fun findUrl(): String? {
            if (intent.data != null)
                return intent.data.toString()
            if (intent.hasExtra(Intent.EXTRA_TEXT))
                return intent.getStringExtra(Intent.EXTRA_TEXT)
            if (preference.contains("URL"))
                return preference.getString("URL", null)
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
