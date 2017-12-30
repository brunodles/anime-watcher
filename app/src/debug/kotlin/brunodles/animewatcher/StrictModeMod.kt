package brunodles.animewatcher

import android.os.StrictMode
import com.brunodles.environmentmods.annotation.ModFor

object StrictModeMod {

    @JvmStatic
    @ModFor(Application::class)
    fun applyThreadPolicy() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyFlashScreen()
                .penaltyDeath()
                .build())
    }

}