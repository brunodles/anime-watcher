package brunodles

import brunodles.animewatcher.explorer.Episode
import com.google.gson.Gson
import kotlin.reflect.KClass

private val gson = Gson()

fun loadResource(path: String): String {
    return ClassLoader.getSystemClassLoader()
        .getResourceAsStream(
            if (path.startsWith('/'))
                path.substring(1)
            else path
        )
        .reader()
        .readText()
}

fun <T> loadJsonResource(path: String, type: Class<T>): T =
    gson.fromJson(loadResource(path), type)

fun <T : Any> String.loadJsonResource(type: KClass<T>): T =
    loadJsonResource(this, type.java)

fun String.loadEpisodeResource() =
    loadJsonResource(this, Episode::class.java)