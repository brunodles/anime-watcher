package brunodles.animewatcher.player

//import brunodles.animewatcher.decoders.UrlChecker
import android.content.Context
import android.util.Log
import brunodles.animewatcher.ImageLoader
import brunodles.animewatcher.api.ApiFactory
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
        return if (episode.isVideoMissing())
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
        Log.d(TAG, "fetchVideo: url: $url")
        return ApiFactory.api.decoder(url)
            .subscribeOn(Schedulers.io())
            .map(::getImageIfNeeded)
            .doOnSuccess { Firebase.addVideo(it) }
            .timeout(1, TimeUnit.MINUTES)
    }

    private fun getImageIfNeeded(episode: Episode): Episode {
        return if (episode.image.isNullOrBlank())
            episode.copy(image = ImageLoader.first("${episode.animeName} ${episode.number} ${episode.description}"))
        else
            episode
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