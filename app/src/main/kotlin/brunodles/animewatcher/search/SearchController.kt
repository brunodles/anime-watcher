package brunodles.animewatcher.search

import android.content.Context
import android.util.Log
import brunodles.animewatcher.BuildConfig
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.player.EpisodeController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import java.net.URLEncoder

internal class SearchController(val context: Context) {
    companion object {
        private const val TAG = "SearchController"
        private const val GOOGLE_SEARCH = "http://google.com/search?q="
    }

    val episodeController: EpisodeController by lazy { EpisodeController(context) }

    fun buildUrl(query: String) = GOOGLE_SEARCH + URLEncoder.encode(
        "$query ${BuildConfig.GOGOLE_QUERY}",
        "UTF-8"
    )

    fun addSearch(query: String) {
    }

    fun onUrl(
        url: String?,
        playerStarter: (Episode) -> Unit,
        urlLoader: (String) -> Unit
    ): Boolean {
        if (url == null)
            return false
        if (url.contains("google.com")) {
            Log.d(TAG, "shouldOverrideUrl: containsGoogle, using default")
            return false
        }

        episodeController.findVideoOn(url)
            .toMaybe()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    playerStarter.invoke(it)
                },
                onError = {
                    urlLoader(url)
                }
            )
        return true
    }
}