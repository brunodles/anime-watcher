package brunodles.animesonlinebr

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimesOnlineBrFactoryTest {

    companion object {

        val VALID_URLS = arrayOf(
                "http://www.animesonlinebr.com.br/video/50034")
        val INVALID_URLS = emptyArray<String>()
        val currentEpisode = Episode(
                number = 1,
                description = "Episódio 01",
                animeName = "Boruto: Naruto Next Generations",
                image = null,
                link = "http://www.animesonlinebr.com.br/video/50034",
                video = "http://www.blogger.com/video-play.mp4?contentId=3dbfcd746b2b8f21&autoplay=true",
                nextEpisodes = arrayListOf(
                        Episode(animeName = "Boruto: Naruto Next Generations",
                                description = "Episódio 02",
                                link = "http://animesonlinebr.com.br/video/50035",
                                number = 2
                        )
                )
        )
    }

    init {
        FactoryChecker.checkFactory(AnimesOnlineBrFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }

}
