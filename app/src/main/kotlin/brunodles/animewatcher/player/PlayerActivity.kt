package brunodles.animewatcher.player

import android.content.Context
import android.content.Intent
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
import brunodles.animewatcher.databinding.ActivityPlayerBinding
import brunodles.animewatcher.databinding.ItemEpisodeBinding
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.parcelable.EpisodeParcel
import brunodles.animewatcher.parcelable.EpisodeParceler
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PlayerActivity : AppCompatActivity() {

    companion object {

        val TAG = "PlayerActivity"
        val STATE_KEY = "episode"
        val EXTRA_EPISODE = "episode"

        fun newIntent(context: Context, episode: Episode): Intent
                = Intent(context, PlayerActivity::class.java)
                .putExtra(EXTRA_EPISODE, EpisodeParceler.toParcel(episode))
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        fun newIntent(context: Context, link: String): Intent
                = Intent(context, PlayerActivity::class.java)
                .setData(Uri.parse(link))
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var player: Player
    private lateinit var cast: Caster
    private var adapter: ViewDataBindingAdapter<Episode, ItemEpisodeBinding>? = null
    private var episode: Episode? = null
    private val episodeController by lazy { EpisodeController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)

        player = Player(this, binding.player)
        setupRecyclerView()

        cast = Caster.Factory.multiCaster(this, binding.chromeCastButton, binding.othersCastButton)

        binding.playRemote.setOnClickListener {
            episode?.let { cast.playRemote(it, player.getCurrentPosition()) }
        }

        val observable = if (intent.hasExtra(EXTRA_EPISODE))
            episodeController.findVideo(intent.getParcelableExtra<EpisodeParcel>(EXTRA_EPISODE))
        else
            episodeController.findVideo(UrlChecker.findUrl(intent))
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(onSuccess = this::onFetchEpisode,
                        onError = this::onError)
    }

    private fun onError(error: Throwable) {
        if (binding.root != null) {
            val snackbar = Snackbar.make(binding.root, "Failed to process the url, ${error.message}", Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("Ok") { snackbar.dismiss() }
            snackbar.show()
        }
        Log.e(TAG, "onResume.FindUrl: ", error)
    }

    private fun onFetchEpisode(episode: Episode) {
        Log.d(TAG, "onFetchEpisode: ")
        episode.video?.let { player.prepareVideo(it) }
        binding.title.text = "${episode.number} - ${episode.description}"
        adapter?.list = episode.nextEpisodes ?: listOf()
        this.episode = episode
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.nextEpisodes.layoutManager = manager
        adapter = ViewDataBindingAdapter<Episode, ItemEpisodeBinding>(R.layout.item_episode) { viewHolder, item, index ->
            viewHolder.binder.description.text = "${item.number} - ${item.description}"
            viewHolder.binder.title.text = item.animeName
            ImageLoader.loadImageInto(item.image, viewHolder.binder.image)
            viewHolder.binder.root.setOnClickListener {
                val intent = newIntent(this, item.link!!)
                startActivity(intent)
            }
        }
        binding.nextEpisodes.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (episode != null)
            outState.putSerializable(STATE_KEY, episode)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState == null)
            return
        episode = savedInstanceState.getSerializable(STATE_KEY) as Episode?
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

    override fun onPause() {
        super.onPause()
        player.stop()
    }

}
