package brunodles.animewatcher.decoders

import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import brunodles.loadEpisodeResource
import brunodles.urlfetcher.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.xit
import org.junit.runner.RunWith
import resource_helper.Resources

@RunWith(Spectrum::class)
class XvideosFactoryTest {

    companion object {
        val VALID_URLS = arrayOf(
            "https://www.xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau",
            "http://www.xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau",
            "xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau"
        )
        val INVALID_URLS = emptyArray<String>()
    }

    init {
        FactoryChecker.describe(XvideosFactory) {
            if (UrlFetcher.useCache)
                whenEpisode(Resources.responses.xvideos.playerEpisodesJson.loadEpisodeResource())
            else
                xit("when episode") {}
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}
