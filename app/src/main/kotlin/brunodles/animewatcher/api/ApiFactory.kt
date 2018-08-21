package brunodles.animewatcher.api

import android.util.Log
import brunodles.animewatcher.explorer.Episode
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object ApiFactory {

    val TAG = "ApiFactory"

    val gson: Gson by lazy {
        GsonBuilder()
            .create()
    }
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor {
                Log.d(TAG, "okHttp: $it")
            }.setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://anime-watcher-spark.herokuapp.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client)
            .build()
    }
    val api: Api by lazy {
        retrofit.create(Api::class.java)
    }

    interface Api {

        @POST("/decoder")
        fun decoder(@Body url: String): Single<Episode>
    }
}

