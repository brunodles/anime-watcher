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
class TvCurseFactoryTest {

    companion object {
        val VALID_URLS = arrayOf(
            "https://tvcurse.com/?p=713",
            "http://tvcurse.com/?p=713",
            "tvcurse.com?p=123",
            "http://tvcurse.com?p=321",
            "animacurse.moe/?p=713",
            "animacurse.tv/?p=713"
        )
        val INVALID_URLS = arrayOf("tvcurse.com/?cat=123")
    }

    init {
        FactoryChecker.describe(TvCurseFactory) {
            describe("when page contains next episodes") {
                whenEpisode(Resources.tvcurse.playerWithNextEpisodesJson.loadEpisodeResource())
            }
            describe("when page does not contains next") {
                whenEpisode(Resources.tvcurse.playerWithoutNextEpisodesJson.loadEpisodeResource())
            }
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}
