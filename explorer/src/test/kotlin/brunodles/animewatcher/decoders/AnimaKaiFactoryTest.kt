package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import org.junit.runner.RunWith

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
        val TSUKIPRO_EPISODE = Episode(
            number = 1,
            description = "Tsukipro The Animation 1 online aqui no site",
            animeName = "Tsukipro The Animation online aqui no site",
            image = "http://www.animekai.info/2017/10/Screenshot_33.jpg",
            link = "https://www.animekaionline.com/tsukipro-the-animation/episodio-1",
            video = "http://www.blogger.com/video-play.mp4?contentId=b186c220e9973f58",
            nextEpisodes = arrayListOf()
        )
        val HIMOUTO_UMARU_CHAN_EPISODE = Episode(
            number = 1,
            description = "Himouto! Umaru-chan 1 online aqui no site",
            animeName = "Himouto! Umaru-chan online aqui no site",
            image = "http/imagens/848x380/",
            link = "https://www.animeskai.net/himouto-umaru-chan/ep-1",
            video = "http://www.blogger.com/video-play.mp4?contentId=a1bc047412d3b897"
        )
        val ERROR_EPISODE = Episode(
            number = 1,
            description = "  online aqui no site",
            link = "https://www.animeskai.net/anime/my-hero/ep-01",
            image = "http/imagens/848x380/",
            video = "",
            animeName = "  online aqui no site"
        )
    }

    init {
        FactoryChecker.describe(AnimaKaiFactory) {
            describe("when page returns 200 (Success)") {
                whenEpisode(TSUKIPRO_EPISODE)
            }
            describe("when page returns 500 (Server Error, but is a success)") {
                whenEpisode(HIMOUTO_UMARU_CHAN_EPISODE)
            }
            describe("when page is empty") {
                whenEpisode(ERROR_EPISODE)
            }
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}
