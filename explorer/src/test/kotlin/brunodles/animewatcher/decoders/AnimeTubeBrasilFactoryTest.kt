package brunodles.animewatcher.decoders

import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import brunodles.loadEpisodeResource
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith
import resource_helper.Resources

@RunWith(Spectrum::class)
class AnimeTubeBrasilFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("https://animetubebrasil.com/1582/")
        val INVALID_URLS = arrayOf("anitub")
    }

    init {
        FactoryChecker.describe(AnimeTubeBrasilFactory) {
            whenEpisode(Resources.animetubebrasil.playerEpisodesJson.loadEpisodeResource())
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}
