package brunodles.animewatcher.decoders

import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import brunodles.loadEpisodeResource
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith
import resource_helper.Resources

@RunWith(Spectrum::class)
class AnimesOnlineBrFactoryTest {

    companion object {

        val VALID_URLS = arrayOf(
            "https://www.animesonlinebr.com.br/video/50034",
            "https://www.animesonlinebr.com.br/desenho/1909"
        )
        val INVALID_URLS = emptyArray<String>()
    }

    init {
        FactoryChecker.describe(AnimesOnlineBrFactory) {
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
            Spectrum.describe("when episode page") {
                whenEpisode(Resources.responses.animesonlinebr.playerEpisodesJson.loadEpisodeResource())
            }
            Spectrum.describe("when anime about page") {
                whenEpisode(Resources.responses.animesonlinebr.aboutPageEpisodesJson.loadEpisodeResource())
            }
        }
    }
}
