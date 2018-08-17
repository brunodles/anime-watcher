package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
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
                video = "https://tamanegi.onepieceex.com.br/episodios/lq/OpEx_208_LQ.mp4?st=Nos67aOS6jKXdpB_2IRZlw&e=1534387490",
                nextEpisodes = arrayListOf(
                        Episode(description = "Next",
                                number = 209,
                                animeName = "One Piece",
                                link = "https://onepiece-ex.com.br/episodios/online/209")
                )
        )
    }

    init {
        FactoryChecker.describe(OnePieceXFactory) {
//            whenEpisode(currentEpisode)
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }

}
