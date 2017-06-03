package brunodles.animewatcher

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import bruno.animewatcher.explorer.AnimeExplorer
import bruno.animewatcher.explorer.CurrentEpisode
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.squareup.picasso.Picasso
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
        val USER_AGENT = "AnimeWatcher"
    }

    private var binding: ActivityMainBinding? = null
    private var player: SimpleExoPlayer? = null
    private var adapter: GenericAdapter<EpisodeLink, ItemEpisodeBinding>? = null
    private var currentEpisode: CurrentEpisode? = null


    var mCastSession: CastSession? = null
    var mSessionManager: SessionManager? = null
//    val mSessionManagerListener: SessionManagerListener = SessionManagerListenerImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        createPlayer()
        setupRecyclerView()

        CastButtonFactory.setUpMediaRouteButton(this, binding?.mediaRouteButton)
        val castContext = CastContext.getSharedInstance(this)

        mSessionManager = castContext.sessionManager
        binding?.playRemote?.setOnClickListener {
            val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)

            movieMetadata.putString(MediaMetadata.KEY_TITLE, currentEpisode?.description)
//            movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, mSelectedMedia.getStudio());
//            movieMetadata.addImage(WebImage(Uri.parse(currentEpisode?.)));
//            movieMetadata.addImage(WebImage(Uri.parse(mSelectedMedia.getImage(1))));

            Log.d(TAG, "onCreate: currentEpisode.video = ${currentEpisode?.video}")
            val mediaInfo = MediaInfo.Builder(currentEpisode?.video)
                    .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                    .setContentType("videos/mp4")
                    .setMetadata(movieMetadata)
//                    .setStreamDuration(mSelectedMedia.getDuration() * 1000)
                    .build()
            val remoteMediaClient = mCastSession?.remoteMediaClient
            val position = player?.currentPosition ?: 0
            remoteMediaClient?.load(mediaInfo, true, position)
            remoteMediaClient?.play()
        }
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.nextEpisodes?.layoutManager = manager
        adapter = GenericAdapter<EpisodeLink, ItemEpisodeBinding>(R.layout.item_episode) { viewHolder, item, index ->
            viewHolder.binder.description.text = item.description
            loadImageInto(item.image, viewHolder.binder.image)
            viewHolder.binder.root.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                        .setData(Uri.parse(item.link))
                startActivity(intent)
            }
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
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        binding?.player?.player = player
    }

    override fun onResume() {
        super.onResume()
        mCastSession = mSessionManager?.currentCastSession
        CheckUrl(intent, this) {
            val episode = it.currentEpisode()
            prepareVideo(episode.video)
            binding?.title?.text = episode.description
            adapter?.list = it.nextEpisodes()
            currentEpisode = episode
        }.execute()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        val landscape = newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (landscape) {
            binding?.otherContent?.visibility = View.GONE
            val params = binding?.player?.layoutParams
            params?.height = ViewGroup.LayoutParams.MATCH_PARENT
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding?.player?.layoutParams = params
            binding?.player?.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            binding?.otherContent?.visibility = View.VISIBLE
            val params = binding?.player?.layoutParams
            params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding?.player?.layoutParams = params
            binding?.player?.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onPause() {
        super.onPause()
        mCastSession = null
        player?.stop()
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
            return factories.firstOrNull { it.isEpisode(url) }?.episode(url)
        }

        override fun onPostExecute(result: AnimeExplorer?) {
            super.onPostExecute(result)
            Log.d(TAG, "onPostExecute " + result)
            if (result != null)
                function(result)
        }
    }
}
