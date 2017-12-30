package brunodles.animewatcher

import android.support.multidex.MultiDexApplication
import cat.ereza.customactivityoncrash.config.CaocConfig
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
        LeakCanary.install(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(!BuildConfig.DEBUG)
        Fabric.with(this, Crashlytics())

        CaocConfig.Builder.create()
                //                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(false) //default: true
                .showErrorDetails(false) //default: true
                .showRestartButton(false) //default: true
                .trackActivities(true) //default: false
                //                .minTimeBetweenCrashesMs(2000) //default: 3000
                //                .errorDrawable(R.drawable.ic_custom_drawable) //default: bug image
                //                .restartActivity(YourCustomActivity::class.java) //default: null (your app's launch activity)
                //                .errorActivity(YourCustomErrorActivity::class.java) //default: null (default error activity)
                //                .eventListener(YourCustomEventListener()) //default: null
                .apply()

        ApplicationMods.apply(this)
    }
}