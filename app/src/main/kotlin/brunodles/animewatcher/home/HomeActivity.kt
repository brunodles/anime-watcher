package brunodles.animewatcher.home

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import brunodles.animewatcher.R
import brunodles.animewatcher.databinding.ActivityHomeBinding
import brunodles.animewatcher.persistence.Preferences
import brunodles.animewatcher.player.VideoActivity

class HomeActivity : Activity() {

    companion object {
        val TAG = "Companion"
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val homeAdapter = HomeAdapter()
        homeAdapter.onAnimeExplorerClickListener = { episode ->
            startActivity(VideoActivity.newIntent(this, episode))
        }
        homeAdapter.onLinkClickListener = { link ->
            startActivity(VideoActivity.newIntent(this, link))
        }
//        homeAdapter.setOn
        homeAdapter.add(LoginRequest())
        Preferences(this).getUrl()?.let { homeAdapter.add(it) }
        binding.recyclerView.adapter = homeAdapter
    }

}