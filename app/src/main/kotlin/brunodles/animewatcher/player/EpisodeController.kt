package brunodles.animewatcher.player

import android.content.Context
import android.util.Log
import brunodles.animewatcher.ImageLoader
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.parcelable.EpisodeParcel
import brunodles.animewatcher.parcelable.EpisodeParceler
import brunodles.animewatcher.persistence.Firebase
import brunodles.rxfirebase.singleObservable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class EpisodeController(val context: Context) {

    companion object {
        val TAG = "EpisodeController"
    }

    fun findVideo(url: String?): Single<Episode> {
        if (url == null) return Single.error(NullPointerException("Empty Url"))

        return findVideoInfo(url)
                .doOnSuccess(this::preFetchNextEpisodes)
                .doOnSuccess {
                    Firebase.addToHistory(url)
                }
    }

    fun findVideo(episode: EpisodeParcel): Single<Episode> {
        if (episode.isInfoMissing())
            return findVideo(episode.link!!)
        return Single.just(episode)
                .subscribeOn(Schedulers.io())
                .map(EpisodeParceler::fromParcel)
                .doOnSuccess {
                    if (episode.link != null)
                        Firebase.addToHistory(episode.link)
                }
    }

    private fun findVideoInfo(url: String): Single<Episode> {
        val ref = Firebase.videoRef(url)
        return ref.singleObservable(Episode::class.java)
                .map {
                    if (it.isPlayable()) it
                    else throw RuntimeException("Episode is not playable!")
                }
                .onErrorResumeNext(fetchVideo(url))
    }

    private fun fetchVideo(url: String): Single<Episode> {
        return Single.just(url)
                .subscribeOn(Schedulers.io())
                .map {
                    UrlChecker.videoInfo(url)
                            ?: throw RuntimeException("Can't find video info")
                }
                .map {
                    if (it.image == null) {
                        Log.d(TAG, "fetchVideo: invalid image $url")
                        Episode(it.description,
                                it.number,
                                it.animeName,
                                ImageLoader.first("${it.animeName} ${it.number} ${it.description}"),
                                it.video,
                                it.link,
                                it.nextEpisodes
                        )
                    } else {
                        it
                    }
                }
                .doOnSuccess { Firebase.addVideo(it) }
                .timeout(1, TimeUnit.MINUTES)
    }

    private fun preFetchNextEpisodes(episode: Episode) {
        if (!episode.containsNextEpisodes()) {
            Log.d(TAG, "preFetchNextEpisodes: nextEpisodes is empty")
            return
        }
        Observable.fromIterable(episode.nextEpisodes)
                .subscribeOn(Schedulers.io())
                .doOnNext { Firebase.addVideo(it) }
                .filter { it.link != null }
                .map { it.link!! }
                .flatMapSingle(this::findVideoInfo)
                .subscribeBy(onNext = {
                    Log.d(TAG, "preFetchNextEpisodes: fetched episode ${it.number}")
                }, onError = {
                    Log.e(TAG, "preFetchNextEpisodes: failed to fetch next episodes", it)
                })
    }

}