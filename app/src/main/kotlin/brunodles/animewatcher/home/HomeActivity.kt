package brunodles.animewatcher.home

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import brunodles.animewatcher.R
import brunodles.animewatcher.databinding.ActivityHomeBinding
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.history.HistoryFragment
import brunodles.animewatcher.nextepisodes.NextEpisodeFragment
import brunodles.animewatcher.player.PlayerActivity
import io.reactivex.disposables.CompositeDisposable

class HomeActivity : AppCompatActivity() {

    companion object {
        val TAG = "HomeActivity"

        fun newIntent(context: Context): Intent? =
            Intent(context, HomeActivity::class.java)
    }

    private lateinit var binding: ActivityHomeBinding
    private val disposable = CompositeDisposable()
    private val historyFragment by lazy { HistoryFragment() }
    private val nextEpisodeFragment by lazy { NextEpisodeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            Log.d(TAG, "onNavigationItemSelected ${resources.getResourceName(it.itemId)}")
            changeFragment(fragmentFor(it.itemId))
            true
        }
        binding.bottomNavigation.selectedItemId = R.id.history
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, "container")
            .commit()
    }

    private fun fragmentFor(itemId: Int): Fragment = when (itemId) {
        R.id.history -> historyFragment
        R.id.nextEpisodes -> nextEpisodeFragment
        else -> historyFragment
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    fun onItemClick(episode: Episode) {
        startActivity(PlayerActivity.newIntent(this, episode))
    }
}
