package brunodles.animewatcher

import brunodles.urlfetcher.UrlFetcher
import com.brunodles.alchemist.Alchemist
import com.brunodles.alchemist.AnnotationInvocation
import com.brunodles.alchemist.AnnotationTransmutation
import com.brunodles.alchemist.TransmutationsBook

object AlchemistFactory {

    private var urlFetcher: UrlFetcher? = null
    val alchemist: Alchemist by lazy {
        System.setProperty(
            "user-agent",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"
        )
        Alchemist.Builder()
            .uriResolver {
                getUrlFetcher().get(it).html()
            }
            .transformers(
                TransmutationsBook.Builder()
                    .add(ToIntTransmutation())
                    .build()
            )
            .build()
    }

    fun getUrlFetcher(): UrlFetcher {
        urlFetcher?.let { return it }
        return UrlFetcher.fetcher()
    }

    fun setUrlFetcher(urlFetcher: UrlFetcher) {
        this.urlFetcher = urlFetcher
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ToInt

class ToIntTransmutation : AnnotationTransmutation<ToInt, List<String>, List<Int>> {

    override fun transform(value: AnnotationInvocation<ToInt, List<String>>): List<Int> =
        value.result.map(String::toInt).toList()
}
