package brunodles.animewatcher.decoders

import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import brunodles.loadEpisodeResource
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.xit
import org.junit.runner.RunWith
import resource_helper.Resources

@RunWith(Spectrum::class)
class AnitubeBrFactoryTest {

    companion object {
        val VALID_URLS = arrayOf(
            "https://www.anitubebr.com/vd/19249/",
            "https://anitubebr.com/vd/19249/",
            "http://www.anitubebr.com/vd/19249/",
            "http://anitubebr.com/vd/19249/",
            "www.anitubebr.com/vd/19249/",
            "anitubebr.com/vd/19249/"
        )
        val INVALID_URLS = arrayOf("anitub")
    }

    init {
        FactoryChecker.describe(AnitubeBrFactory) {
            whenEpisode(Resources.anitubebr.playerEpisodesJson.loadEpisodeResource())
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
            xit("check video url") {}
        }
    }
}