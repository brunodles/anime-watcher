package brunodles.animewatcher

import com.brunodles.environmentmods.annotation.ModFor
import me.drakeet.library.CrashWoodpecker
import me.drakeet.library.PatchMode

object CrashWoopeckerMod {

    @JvmStatic
    @ModFor(Application::class)
    fun initCrashWoodpecker(application: Application) {
        CrashWoodpecker.instance()
                .withKeys("widget", "me.drakeet")
                .setPatchMode(PatchMode.SHOW_LOG_PAGE)
                .setPatchDialogUrlToOpen("https://drakeet.me")
                .setPassToOriginalDefaultHandler(true)
                .flyTo(application)
    }
}