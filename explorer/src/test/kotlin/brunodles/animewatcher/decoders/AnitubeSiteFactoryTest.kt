package brunodles.animewatcher.decoders

import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import brunodles.loadEpisodeResource
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith
import resource_helper.Resources

@RunWith(Spectrum::class)
class AnitubeSiteFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("https://www.anitube.site/765/")
        val INVALID_URLS = arrayOf("anitub")
    }

    init {
        FactoryChecker.describe(AnitubeSiteFactory) {
            whenEpisode(Resources.anitubesite.playerEpisodesJson.loadEpisodeResource())
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}