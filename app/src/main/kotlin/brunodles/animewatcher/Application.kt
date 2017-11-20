package brunodles.animewatcher

import android.app.Application
import android.os.StrictMode
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.google.firebase.database.FirebaseDatabase
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
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

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .penaltyDeath()
                    .build())
        }
    }
}