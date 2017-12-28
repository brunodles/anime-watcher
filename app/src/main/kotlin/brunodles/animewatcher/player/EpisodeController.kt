package brunodles.animewatcher.player

import android.content.Context
import android.util.Log
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.parcelable.EpisodeParcel
import brunodles.animewatcher.parcelable.EpisodeParceler
import brunodles.animewatcher.persistence.Firebase
import brunodles.animewatcher.persistence.Preferences
import brunodles.rxfirebase.singleObservable
import com.brunodles.googleimagesapi.ImagesApi
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import com.brunodles.googleimagesapi.PageFetcher
import okhttp3.Request
import java.io.IOException


class EpisodeController(val context: Context) {

    companion object {
        val TAG = "EpisodeController"
    }

    object imagesPageFetcher : PageFetcher {
        override fun fetchPage(url: String): String? {
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .build()
            return try {
                val response = client.newCall(request).execute()
                response.body().string()
            } catch (e: IOException) {
                null
            }

        }
    }

    fun findVideo(url: String?): Observable<Episode> {
        if (url == null) return Observable.error(NullPointerException("Empty Url"))

        return findVideoInfo(url)
                .doOnNext(this::preFetchNextEpisodes)
                .doOnNext {
                    Preferences(context).setUrl(url)
                    Firebase.addToHistory(url)
                }
    }

    fun findVideo(episode: EpisodeParcel): Observable<Episode> {
        if (episode.isInfoMissing())
            return findVideo(episode.link!!)
        return Observable.just(episode)
                .map(EpisodeParceler::fromParcel)
                .doOnNext {
                    if (episode.link != null) {
                        Preferences(context).setUrl(episode.link)
                        Firebase.addToHistory(episode.link)
                    }
                }
    }

    private fun findVideoInfo(url: String): Observable<Episode> {
        Log.d(TAG, "findVideoInfo: find video on '$url'")
        val ref = Firebase.videoRef(url)
        return ref.singleObservable(Episode::class.java)
                .map {
                    if (it.isPlayable()) it
                    else throw RuntimeException("Episode is not playable!")
                }
                .doOnNext { Log.d(TAG, "findVideoInfo: found episode ${it.number}") }
                .onErrorResumeNext(fetchVideo(url))
                .map { it ?: throw RuntimeException("Can't find video info") }
    }

    private fun fetchVideo(url: String): Observable<Episode> {
        return Observable.just(url)
                .observeOn(Schedulers.io())
                .map {
                    CheckUrl.videoInfo(url)
                            ?: throw RuntimeException("Can't find video info")
                }
                .map {
                    if (it.image == null)
                        Episode(it.description,
                                it.number,
                                it.animeName,
                                ImagesApi.queryBuilder(imagesPageFetcher)
                                        .query("${it.animeName} ${it.number} ${it.description}")
                                        .listImageUrls()
                                        .first(),
                                it.video,
                                it.link,
                                it.nextEpisodes
                        )
                    else
                        it
                }
                .doOnNext { Firebase.addVideo(it) }
    }

    private fun preFetchNextEpisodes(episode: Episode) {
        Log.d(TAG, "preFetchNextEpisodes: $episode")
        if (!episode.containsNextEpisodes()) {
            Log.d(TAG, "preFetchNextEpisodes: nextEpisodes is empty")
            return
        }
        Observable.fromIterable(episode.nextEpisodes)
                .doOnNext { Firebase.addVideo(it) }
                .filter { it.link != null }
                .map { it.link!! }
                .flatMap(this::findVideoInfo)
                .subscribeBy(onNext = {
                    Log.d(TAG, "preFetchNextEpisodes: fetched episode ${it.number}")
                }, onError = {
                    Log.e(TAG, "preFetchNextEpisodes: failed to fetch next episodes", it)
                })
    }

}