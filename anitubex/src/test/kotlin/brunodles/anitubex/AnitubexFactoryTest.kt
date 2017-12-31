package brunodles.anitubex

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnitubexFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("http://www.anitubex.com/one-piece-1",
                "https://www.anitubex.com/one-piece-1")
        val INVALID_URLS = arrayOf("anitub")
        val currentEpisode = Episode(
                number = 1,
                description = "One Piece 1",
                link = "http://www.anitubex.com/one-piece-1",
                video = "http://www.blogger.com/video-play.mp4?contentId=3eb2b428663ef38f",
                nextEpisodes = arrayListOf(
                        Episode(number = 2,
                                description = "Proximo Episódio",
                                link = "http://www.anitubex.net/video/66822"
                        )
                )
        )
    }

    init {
        FactoryChecker.checkFactory(AnitubexFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }
}