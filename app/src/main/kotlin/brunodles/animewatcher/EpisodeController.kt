package brunodles.animewatcher

import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import bruno.animewatcher.explorer.AnimeExplorer
import brunodles.rxfirebase.singleObservable
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class EpisodeController(val context: Context) {

    companion object {
        val TAG = "EpisodeController"
    }

    fun findVideo(intent: Intent): Observable<AnimeExplorer> {
        val url = getUrl(intent)
        if (url == null) return Observable.empty()

        return findVideoInfo(url)
                .doOnNext(this::fetchNextEpisodes)
                .doOnNext {
                    PreferenceManager.getDefaultSharedPreferences(context)
                            .edit()
                            .putString("URL", url)
                            .apply()
                }
    }

    private fun getUrl(intent: Intent): String? = CheckUrl.findUrl(intent)
            ?: PreferenceManager.getDefaultSharedPreferences(context).getString("URL", null)

    private fun findVideoInfo(url: String): Observable<AnimeExplorer> {
        Log.d(TAG, "findVideoInfo: find video on '$url'")
        val ref = FirebaseDatabase.getInstance().getReference("video").child(fixUrlToFirebase(url))
        return ref.singleObservable(AnimeExplorer::class.java)
                .onErrorResumeNext(
                        Observable.just(url)
                                .observeOn(Schedulers.io())
                                .map { CheckUrl.videoInfo(url) }
                                .map { it ?: throw RuntimeException("Can't find video info") }
                                .doOnNext { ref.setValue(it) }
                )
                .map { it ?: throw RuntimeException("Can't find video info") }
    }

    private fun fetchNextEpisodes(it: AnimeExplorer) {
        Observable.fromIterable(it.nextEpisodes)
                .map { it.link }
                .flatMap(this::findVideoInfo)
                .subscribeBy(
                        onNext = {
                            Log.d(TAG, "fetchNextEpisodes: fetched episode ${it.currentEpisode}")
                        },
                        onError = {
                            Log.e(TAG, "fetchNextEpisodes: failed to fetch next episodes", it)
                        })
    }

}