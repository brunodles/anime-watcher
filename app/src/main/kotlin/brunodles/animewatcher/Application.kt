package brunodles.animewatcher

import android.support.multidex.MultiDexApplication
import brunodles.urlfetcher.UrlFetcher
import com.brunodles.environmentmods.annotation.Moddable
import com.crashlytics.android.Crashlytics
import com.google.firebase.database.FirebaseDatabase
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric

@Moddable
class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        UrlFetcher.cacheDir = this.cacheDir.path
        LeakCanary.install(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(!BuildConfig.DEBUG)
        Fabric.with(this, Crashlytics())

        ApplicationMods.apply(this)
    }
}