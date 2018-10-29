package brunodles.animewatcher.decoders

import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import brunodles.loadEpisodeResource
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import org.junit.runner.RunWith
import resource_helper.Resources

@RunWith(Spectrum::class)
class AnimaKaiFactoryTest {

    companion object {
        val VALID_URLS = arrayOf(
            "https://www.animekaionline.com/tsukipro-the-animation/episodio-1",
            "http://www.animeskai.com/himouto-umaru-chan/ep-1",
            "https://www.animeskai.net/himouto-umaru-chan/ep-1",
            "animakai.info/himouto-umaru-chan/ep-1"
        )
        val INVALID_URLS = emptyArray<String>()
    }

    init {
        FactoryChecker.describe(AnimaKaiFactory) {
            describe("when page returns 200 (Success)") {
                whenEpisode(Resources.animakai.tskiproEpisodesJson.loadEpisodeResource())
            }
            describe("when page returns 500 (Server Error, but is a success)") {
                whenEpisode(Resources.animakai.umaruchanEpisodesJson.loadEpisodeResource())
            }
            describe("when page is empty") {
                whenEpisode(Resources.animakai.errorEpisodesJson.loadEpisodeResource())
            }
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}
