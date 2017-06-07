package brunodles.animewatcher

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
import bruno.animewatcher.explorer.CurrentEpisode
import bruno.animewatcher.explorer.EpisodeLink
import brunodles.animewatcher.databinding.ActivityMainBinding
import brunodles.animewatcher.databinding.ItemEpisodeBinding
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private var binding: ActivityMainBinding? = null
    private var adapter: GenericAdapter<EpisodeLink, ItemEpisodeBinding>? = null
    private var currentEpisode: CurrentEpisode? = null
    private var player: Player? = null
    private var cast: Cast? = null
    private val episodeController by lazy { EpisodeController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        player = Player(this, binding?.player)
        setupRecyclerView()

        cast = Cast(this, binding?.mediaRouteButton)

        binding?.playRemote?.setOnClickListener {
            cast?.playRemove(currentEpisode, player?.getCurrentPosition() ?: 0)
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

    override fun onStart() {
        super.onStart()
        episodeController.findVideo(intent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(onNext = {
                    val episode = it.currentEpisode()
                    player?.prepareVideo(episode.video)
                    binding?.title?.text = episode.description
                    adapter?.list = it.nextEpisodes()
                    currentEpisode = episode
                }, onError = {
                    if (binding?.root != null) {
                        val snackbar = Snackbar.make(binding!!.root, "Failed to process the url, ${it.message}", Snackbar.LENGTH_INDEFINITE)
                        snackbar.setAction("Ok") { snackbar.dismiss() }
                        snackbar.show()
                    }
                    Log.e(TAG, "onResume.FindUrl: ", it)
                })
    }

    override fun onResume() {
        super.onResume()
        cast?.onResume()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        val landscape = newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (landscape) {
            binding?.otherContent?.visibility = View.GONE
            val params = binding?.player?.layoutParams
            params?.height = ViewGroup.LayoutParams.MATCH_PARENT
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding?.player?.layoutParams = params
            binding?.player?.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions

        } else {
            binding?.otherContent?.visibility = View.VISIBLE
            val params = binding?.player?.layoutParams
            params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding?.player?.layoutParams = params
            binding?.player?.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH)
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun onPause() {
        super.onPause()
        cast?.onPause()
        player?.stop()
    }

}
