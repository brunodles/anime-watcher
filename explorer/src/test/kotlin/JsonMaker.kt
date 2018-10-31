import brunodles.animewatcher.decoders.XvideosFactoryTest
import brunodles.animewatcher.explorer.BuildConfig
import com.google.gson.Gson
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import kotlin.reflect.KClass

@Ignore
@RunWith(JUnit4::class)
class JsonMaker {
    private val gson = Gson()
    private var dir: File? = null
    private var dirname: String? = null

    @Test
    fun main() {
        forClass(XvideosFactoryTest::class, XvideosFactoryTest) {
            //            writeEpisode("player", currentEpisode)
//            writeEpisode("player_without_next", PLAYER_WITHOUT_NEXT)
        }
    }

    private fun <T : Any> forClass(factoryTest: KClass<out Any>, obj: T, func: T.() -> Unit) {
        dirname = factoryTest.java.simpleName.replace("FactoryTest", "")
            .toLowerCase()
        dir =
            File(BuildConfig.ROOT_DIR, "/explorer/src/test/resources/$dirname")
        func.invoke(obj)
    }

    private fun writeEpisode(prefix: String, obj: Any) =
        writeFile(prefix + "_episodes.json", obj)

    private fun writeFile(filename: String, obj: Any) {
        val json = gson.toJson(obj)
//        println("$filename = $json")
        val fieldName =
            filename.split(Regex("[^a-zA-Z0-9]+"))
                .asSequence()
                .map { it.capitalize() }
                .joinToString("")
                .decapitalize()
        println("$filename = Resources.$dirname.$fieldName.loadEpisodeResource()")
        dir?.mkdirs()
        File(dir, filename).writeText(json)
    }
}
