package brunodles.animewatcher.player

import android.content.Context
import android.content.Intent
import android.util.Log
import brunodles.animewatcher.explorer.AnimeExplorer
import brunodles.animewatcher.fixUrlToFirebase
import brunodles.animewatcher.persistence.Preferences
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
                .doOnNext { Preferences(context).setUrl(url) }
    }

    private fun getUrl(intent: Intent): String? = CheckUrl.findUrl(intent)
            ?: Preferences(context).getUrl()

    private fun findVideoInfo(url: String): Observable<AnimeExplorer> {
        Log.d(TAG, "findVideoInfo: find video on '$url'")
        val ref = FirebaseDatabase.getInstance().getReference("video").child(fixUrlToFirebase(url))
        return ref.singleObservable(AnimeExplorer::class.java)
                .onErrorResumeNext(
                        Observable.just(url)
                                .observeOn(Schedulers.io())
                                .map {
                                    CheckUrl.videoInfo(url)
                                            ?: throw RuntimeException("Can't find video info")
                                }
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