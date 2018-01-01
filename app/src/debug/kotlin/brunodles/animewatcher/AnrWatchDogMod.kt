package brunodles.animewatcher

import com.brunodles.environmentmods.annotation.ModFor
import com.github.anrwatchdog.ANRWatchDog

object AnrWatchDogMod {

    private val TIMEOUT = 10000

    @JvmStatic
    @ModFor(Application::class)
    fun initCrashWoodpecker(){
        ANRWatchDog(TIMEOUT)
                .setIgnoreDebugger(true)
                .start()
    }
}