package brunodles.animewatcher

import android.os.Environment
import com.brunodles.environmentmods.annotation.ModFor
import java.io.File


object LocalCrashes {

    @JvmStatic
    @ModFor(Application::class)
    fun installLocalCrashHandler(application: Application) {
        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            val dir = File(Environment.getExternalStorageDirectory(), application.packageName)
            if (!dir.exists())
                dir.mkdirs()
            val file = File(dir, "${System.currentTimeMillis()}-${throwable.message}")
            val stackTrace = throwable.stackTrace.joinToString(" ")
            file.writeText(stackTrace)
            exceptionHandler.uncaughtException(thread, throwable)
        }
    }

    fun isExternalStorageWritable(): Boolean
            = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

}