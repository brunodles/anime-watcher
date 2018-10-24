package brunodles.animewatcher

import brunodles.urlfetcher.UrlFetcher
import com.brunodles.alchemist.Alchemist
import com.brunodles.alchemist.AnnotationInvocation
import com.brunodles.alchemist.AnnotationTransmutation
import com.brunodles.alchemist.TransmutationsBook

object AlchemistFactory {

    var urlFetcher: UrlFetcher = UrlFetcher.fetcher()

    val alchemist: Alchemist by lazy {
        System.setProperty(
            "user-agent",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"
        )
        Alchemist.Builder()
            .uriResolver {
                urlFetcher.get(it).html()
            }
            .transformers(
                TransmutationsBook.Builder()
                    .add(ToIntTransmutation())
                    .build()
            )
            .build()
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ToInt

class ToIntTransmutation : AnnotationTransmutation<ToInt, List<String>, List<Int>> {

    override fun transform(value: AnnotationInvocation<ToInt, List<String>>): List<Int> =
        value.result.map(String::toInt).toList()
}