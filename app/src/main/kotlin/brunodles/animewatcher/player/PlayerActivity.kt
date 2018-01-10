package brunodles.animewatcher.player

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import brunodles.adapter.ViewDataBindingAdapter
import brunodles.animewatcher.ImageLoader
import brunodles.animewatcher.R
import brunodles.animewatcher.cast.Caster
import brunodles.animewatcher.cast.isConnected
import brunodles.animewatcher.databinding.ActivityPlayerBinding
import brunodles.animewatcher.databinding.ItemEpisodeBinding
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.parcelable.EpisodeParcel
import brunodles.animewatcher.parcelable.EpisodeParceler
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PlayerActivity : AppCompatActivity() {

    companion object {

        val TAG = "PlayerActivity"
        val STATE_EPISODE = "episode"
        val STATE_POSITION = "position"
        val EXTRA_EPISODE = "episode"
        val PREF_VIDEO = "video"
        val PREF_POSITION = "position"

        fun newIntent(context: Context, episode: Episode): Intent
                = Intent(context, PlayerActivity::class.java)
                .putExtra(EXTRA_EPISODE, EpisodeParceler.toParcel(episode))

        fun newIntent(context: Context, link: String): Intent
                = Intent(context, PlayerActivity::class.java)
                .setData(Uri.parse(link))
    }

    private lateinit var binding: ActivityPlayerBinding
    private var player: Player? = null
    private var caster: Caster? = null
    private var adapter: ViewDataBindingAdapter<Episode, ItemEpisodeBinding>? = null
    private var episode: Episode? = null
    private val episodeController by lazy { EpisodeController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)
        setupRecyclerView()

        binding.playRemote.setOnClickListener {
            episode?.let { caster?.playRemote(it, player.getCurrentPosition()) }
        }

        sharedPreferences().edit().clear().apply()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        sharedPreferences().edit().clear().apply()
    }

    private fun createPlayer(episode: Episode?) {
        player = Player(this, binding.player)
        player?.onEndListener = {
            episode?.nextEpisodes?.firstOrNull()?.let {
                startActivity(newIntent(this, it))
            }
        }
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.nextEpisodes.layoutManager = manager
        adapter = ViewDataBindingAdapter<Episode, ItemEpisodeBinding>(R.layout.item_episode) { viewHolder, item, _ ->
            viewHolder.binder.description.text = "${item.number} - ${item.description}"
            viewHolder.binder.title.text = item.animeName
            ImageLoader.loadImageInto(item.image, viewHolder.binder.image)
            viewHolder.binder.root.setOnClickListener {
                startActivity(newIntent(this, item.link))
            }
        }
        binding.nextEpisodes.adapter = adapter
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState == null)
            return
        episode = savedInstanceState.getSerializable(STATE_EPISODE) as Episode?
        episode?.video?.let { player?.prepareVideo(it) }
    }

    override fun onStart() {
        super.onStart()
        caster = Caster.Factory.multiCaster(this, binding.chromeCastButton, binding.othersCastButton)

        caster?.setOnEndListener {
            episode?.nextEpisodes?.firstOrNull()?.let {
                startActivity(newIntent(this, it))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: isEpisodeNull? ${episode == null}")

        val observable = if (intent.hasExtra(EXTRA_EPISODE))
            episodeController.findVideoOn(intent.getParcelableExtra<EpisodeParcel>(EXTRA_EPISODE))
        else
            episodeController.findVideoOn(UrlChecker.findUrl(intent))
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(onSuccess = this::onFetchEpisode,
                        onError = this::onError)
        val preferences = sharedPreferences()
        preferences.getString(PREF_VIDEO, null)?.also { url ->
            if (player == null)
                createPlayer(episode)
            player?.prepareVideo(url)
            player?.seekTo(preferences.getLong(PREF_POSITION, C.TIME_UNSET))
        }
    }

    private fun onFetchEpisode(episode: Episode) {
        Log.d(TAG, "onFetchEpisode: ")
        episode.video?.let {
            if (player == null)
                createPlayer(episode)
            if (caster.isConnected()) {
                player?.prepareVideo(it, playWhenReady = false)
                caster?.playRemote(episode, 0L)
            } else {
                player?.prepareVideo(it)
            }
        }
        binding.title.text = "${episode.number} - ${episode.description}"
        adapter?.list = episode.nextEpisodes ?: listOf()
        this.episode = episode
    }

    private fun onError(error: Throwable) {
        if (binding.root != null) {
            val snackbar = Snackbar.make(binding.root, "Failed to process the url, ${error.message}", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("Ok") { snackbar.dismiss() }
            snackbar.show()
        }
        Log.e(TAG, "onResume.FindUrl: ", error)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences().editAndApply {
            putString(PREF_VIDEO, episode?.link)
            putLong(PREF_POSITION, player.getCurrentPosition())
        }
        player?.stopAndRelease()
        player = null
    }

    override fun onStop() {
        super.onStop()
        caster?.setOnEndListener(null)
        caster = null
    }

    private fun sharedPreferences() = getSharedPreferences("PlayerActivity", Context.MODE_PRIVATE)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (episode != null)
            outState.putSerializable(STATE_EPISODE, episode)
        outState.putLong(STATE_POSITION, player.getCurrentPosition())
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        val landscape = newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (landscape) {
            binding.otherContent.visibility = View.GONE
            val params = binding.player.layoutParams
            params?.height = ViewGroup.LayoutParams.MATCH_PARENT
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.player.layoutParams = params
            binding.player.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions

        } else {
            binding.otherContent.visibility = View.VISIBLE
            val params = binding.player.layoutParams
            params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.player.layoutParams = params
            binding.player.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
        super.onConfigurationChanged(newConfig)
    }
}

private fun SharedPreferences.editAndApply(function: SharedPreferences.Editor.() -> Unit)
        = this.edit().also { function.invoke(it) }.apply()

