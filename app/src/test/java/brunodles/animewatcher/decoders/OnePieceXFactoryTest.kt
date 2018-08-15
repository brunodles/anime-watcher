package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.urlfetcher.UrlFetcher
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class OnePieceXFactoryTest {

    companion object {

        val VALID_URLS = arrayOf(
                "https://onepiece-ex.com.br/episodios/online/208/",
                "http://one-piece-x.com.br/episodios/online/207/",
                "one-piece-x.com.br/episodios/online/209")
        val INVALID_URLS = arrayOf(
                "https://one-piece-x.com.br/episodios/t04/",
                "one-piece-x.com.br/episodios/online")

        val currentEpisode = Episode(
                number = 208,
                animeName = "One Piece",
                description = "Os piratas do Foxy!! A Davy Back!",
                link = "https://one-piece-x.com.br/episodios/online/208/",
                video = "https://piiman.onepieceex.com.br/episodios/lq/OpEx_208_LQ.mp4?st=lNOi33kxk6atLll7_UdmJw&e=1534322656",
                nextEpisodes = arrayListOf(
                        Episode(description = "Next",
                                number = 209,
                                animeName = "One Piece",
                                link = "https://onepiece-ex.com.br/episodios/online/209")
                )
        )
    }

    init {
        UrlFetcher.useCache = true
        FactoryChecker.checkFactory(OnePieceXFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }

}
