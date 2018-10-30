package brunodles.animewatcher.decoders

import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import brunodles.loadEpisodeResource
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith
import resource_helper.Resources

@RunWith(Spectrum::class)
class OnePieceXFactoryTest {

    companion object {

        val VALID_URLS = arrayOf(
            "https://onepiece-ex.com.br/episodios/online/208/",
            "http://one-piece-x.com.br/episodios/online/207/",
            "one-piece-x.com.br/episodios/online/209"
        )
        val INVALID_URLS = arrayOf(
            "https://one-piece-x.com.br/episodios/t04/",
            "one-piece-x.com.br/episodios/online"
        )
    }

    init {
        FactoryChecker.describe(OnePieceXFactory) {
            whenEpisode(Resources.responses.onepiecex.playerEpisodesJson.loadEpisodeResource())
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}
