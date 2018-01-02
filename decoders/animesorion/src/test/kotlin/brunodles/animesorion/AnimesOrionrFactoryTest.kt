package brunodles.animacurse

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimesOrionrFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("http://www.animesorion.tv/71672")
        val INVALID_URLS = arrayOf("animacurse.moe/?cat=123")
        val currentEpisode = Episode(
                number = 2,
                description = "Episódio 2",
                animeName = "Boku No Hero Academia",
                image = null,
                link = "http://www.animesorion.tv/71672",
                video = "https://www.blogger.com/video-play.mp4?contentId=b0bccad36e2be8e1",
                nextEpisodes = arrayListOf(
                        Episode(number = 3,
                                description = "Episódio 3",
                                animeName = "Boku No Hero Academia",
                                image = null,
                                link = "http://www.animesorion.tv/71808")
                )
        )
    }

    init {
        FactoryChecker.checkFactory(AnimesOrionFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }
}