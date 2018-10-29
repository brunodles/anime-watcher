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
class AnimesOrionFactoryTest {

    companion object {
        val VALID_URLS = arrayOf(
            "http://www.animesorion.online/71672",
            "http://www.animesorion.tv/71672",
            "https://www.animesorion.site/71672",
            "https://www.animesorion.online/71808",
            "https://www.animesorion.org/71808",
            "https://www.animesorion.org/71808?blabalba",
            "https://www.animesorion.site/71672/18092"
        )
        val INVALID_URLS = emptyArray<String>()
    }

    init {
        FactoryChecker.describe(AnimesOrionFactory) {
            describe("when single next episodes") {
                whenEpisode(Resources.animesorion.singleNextEpisodesJson.loadEpisodeResource())
            }
            describe("when about page") {
                whenEpisode(Resources.animesorion.aboutPageEpisodesJson.loadEpisodeResource())
            }
            describe("when no next episodes") {
                whenEpisode(Resources.animesorion.noNextEpisodesJson.loadEpisodeResource())
            }
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}