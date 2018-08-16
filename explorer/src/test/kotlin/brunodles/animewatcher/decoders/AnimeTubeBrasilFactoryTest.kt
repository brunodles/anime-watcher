package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimeTubeBrasilFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("https://animetubebrasil.com/1582/"
        )
        val INVALID_URLS = arrayOf("anitub")
        val currentEpisode = Episode(
                number = 1,
                description = "Eu Sou Ruffy!",
                link = "https://animetubebrasil.com/1582/",
                video = "https://www.blogger.com/video-play.mp4?contentId=3eb2b428663ef38f",
                animeName = "One Piece",
                nextEpisodes = arrayListOf(
                        Episode(number = 2,
                                animeName = "One Piece",
                                description = "Next",
                                link = "https://animetubebrasil.com/1583/"
                        )
                )
        )
    }

    init {
        FactoryChecker.checkFactory(AnimeTubeBrasilFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }
}