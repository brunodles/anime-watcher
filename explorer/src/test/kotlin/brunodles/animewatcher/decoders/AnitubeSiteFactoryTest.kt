package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnitubeSiteFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("https://www.anitube.site/765/")
        val INVALID_URLS = arrayOf("anitub")
        val currentEpisode = Episode(
            number = 1,
            description = "Eu Sou Ruffy!",
            link = "https://www.anitube.site/765/",
            video = "https://www.blogger.com/video-play.mp4?contentId=3eb2b428663ef38f",
            image = "https://www.anitube.site/player/img/Capa-Player-Cine.png",
            animeName = "One Piece",
            nextEpisodes = arrayListOf(
                Episode(
                    number = 2,
                    animeName = "One Piece",
                    description = "O Grande Espadachim Aparece!",
                    link = "https://www.anitube.site/802/"
                )
            )
        )
    }

    init {
        FactoryChecker.describeFactory(AnitubeSiteFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }
}