package brunodles.animewatcher

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.brunodles.googleimagesapi.ImagesApi
import com.brunodles.googleimagesapi.PageFetcher
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.lang.ref.WeakReference

object ImageLoader {
    private var picassoSingleton: WeakReference<Picasso>? = null

    fun picasso(context: Context): Picasso {
        if (picassoSingleton?.get() == null)
            picassoSingleton = WeakReference(Picasso.Builder(context)
                    .indicatorsEnabled(BuildConfig.DEBUG && BuildConfig.IMAGE_LOADER_LOGGGIN_ENABLED)
                    .loggingEnabled(BuildConfig.IMAGE_LOADER_LOGGGIN_ENABLED)
                    .defaultBitmapConfig(Bitmap.Config.RGB_565)
                    .build())
        return picassoSingleton?.get()!!
    }

    fun loadImageInto(url: String?, image: ImageView) {
        if (url.isNullOrEmpty()) return
        picasso(image.context)
                .load(url)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_error)
                .into(image)
    }

    fun fetch(context: Context, url: String) =
            picasso(context)
                    .load(url)
                    .fetch()

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

    fun first(query: String): String? = search(query).first()

    fun search(query: String) =
            ImagesApi.queryBuilder(imagesPageFetcher)
                    .query(query)
                    .size(640, 480) // TODO: try to parametrize this with dimens
                    .listImageUrls()

    fun searchObservable(query: String) =
            Observable.just(query)
                    .subscribeOn(Schedulers.io())
                    .map {
                        ImagesApi.queryBuilder(imagesPageFetcher)
                                .query(it)
                    }
}