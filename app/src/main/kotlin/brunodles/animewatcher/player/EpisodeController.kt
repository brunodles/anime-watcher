package brunodles.animewatcher.player

import android.content.Context
import android.util.Log
import brunodles.animewatcher.ImageLoader
import brunodles.animewatcher.decoders.UrlChecker
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

    fun findVideoOn(url: String?): Single<Episode> {
        if (url == null) return Single.error(NullPointerException("Empty Url"))

        return checkRemoveVideoInfo(url)
                .doOnSuccess(this::preFetchNextEpisodes)
                .doOnSuccess {
                    Firebase.addToHistory(url)
                }
    }

    fun findVideoOn(episode: EpisodeParcel): Single<Episode> {
        return if (episode.isInfoMissing())
            findVideoOn(episode.link)
        else
            Single.just(episode)
                    .subscribeOn(Schedulers.io())
                    .map(EpisodeParceler::fromParcel)
                    .doOnSuccess { Firebase.addToHistory(episode.link) }
    }

    private fun checkRemoveVideoInfo(url: String): Single<Episode> {
        return Firebase.videoRef(url).singleObservable(Episode::class.java)
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
                    if (it.image.isNullOrBlank()) {
                        Log.d(TAG, "fetchVideo: invalid image $url")
                        it.copy(image = ImageLoader.first("${it.animeName} ${it.number} ${it.description}"))
                    } else {
                        it
                    }
                }
                .doOnSuccess { Firebase.addVideo(it) }
                .timeout(1, TimeUnit.MINUTES)
    }

    private fun preFetchNextEpisodes(episode: Episode) {
        if (!episode.containsNextEpisodes()) {
            Log.d(TAG, "preFetchNextEpisodes: nextEpisode is empty")
            return
        }
        Observable.fromIterable(episode.nextEpisodes)
                .subscribeOn(Schedulers.io())
                .doOnNext { Firebase.addVideo(it) }
                .flatMapSingle { fetchVideo(it.link) }
                .subscribeBy(onNext = {
                    Firebase.addVideo(it)
                }, onError = {
                    Log.e(TAG, "preFetchNextEpisodes: failed to fetch next episodes", it)
                })
    }

}