package brunodles.animewatcher

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        Fabric.with(this, Crashlytics())
    }
}