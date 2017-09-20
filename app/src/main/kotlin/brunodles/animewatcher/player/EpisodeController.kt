package brunodles.animewatcher.player

import android.content.Context
import android.content.Intent
import android.util.Log
import brunodles.animewatcher.BuildConfig
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.persistence.Firebase
import brunodles.animewatcher.persistence.Preferences
import brunodles.rxfirebase.singleObservable
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class EpisodeController(val context: Context) {

    companion object {
        val TAG = "EpisodeController"
    }

    fun findVideo(intent: Intent): Observable<Episode> {
        val url = getUrl(intent)
        if (url == null) return Observable.empty()

        return findVideoInfo(url)
                .doOnNext(this::fetchNextEpisodes)
                .doOnNext { Preferences(context).setUrl(url) }
                .doOnNext { Firebase.addToHistory(url) }
    }

    private fun getUrl(intent: Intent): String? = CheckUrl.findUrl(intent)
            ?: Preferences(context).getUrl()

    private fun findVideoInfo(url: String): Observable<Episode> {
        Log.d(TAG, "findVideoInfo: find video on '$url'")
        val ref = Firebase.videoRef(url)
        return ref.singleObservable(Episode::class.java)
                .doOnNext { Log.d(TAG, "findVideoInfo: found episode ${it.number}") }
                .onErrorResumeNext(
                        Observable.just(url)
                                .observeOn(Schedulers.io())
                                .map {
                                    CheckUrl.videoInfo(url)
                                            ?: throw RuntimeException("Can't find video info")
                                }
//                                .doOnNext { ref.setValue(it) }
                                .doOnNext { Firebase.addVideo(it) }
                )
                .map { it ?: throw RuntimeException("Can't find video info") }
    }

    private fun fetchNextEpisodes(episode: Episode) {
        Log.d(TAG, "fetchNextEpisodes: $episode")
        if (episode.nextEpisodes == null) return
        Observable.fromIterable(episode.nextEpisodes)
                .filter { it.link != null }
                .map { it.link!! }
                .flatMap(this::findVideoInfo)
                .subscribeBy(
                        onNext = {
                            if (BuildConfig.DEBUG)
                                Log.d(TAG, "fetchNextEpisodes: fetched episode $it")
                        },
                        onError = {
                            Log.e(TAG, "fetchNextEpisodes: failed to fetch next episodes", it)
                        })
    }

}