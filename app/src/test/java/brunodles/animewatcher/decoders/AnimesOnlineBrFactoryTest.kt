package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimesOnlineBrFactoryTest {

    companion object {

        val ANIME_NAME = "Boruto"
        val VALID_URLS = arrayOf(
                "https://www.animesonlinebr.com.br/video/50034")
        val INVALID_URLS = emptyArray<String>()
        val ESPISODE_AND_SINGLE = Episode(
                number = 1,
                description = "Episódio 01 – Eu sou Uzumaki Boruto!! online, Boruto - Episódio 01 – Eu sou Uzumaki Boruto!! Online",
                animeName = ANIME_NAME,
                image = null,
                link = "https://www.animesonlinebr.com.br/video/50034",
                video = "https://www.blogger.com/video-play.mp4?contentId=3dbfcd746b2b8f21",
                nextEpisodes = arrayListOf(
                        Episode(animeName = ANIME_NAME,
                                description = "Boruto -  Episódio 02 – O Filho do Hokage!!",
                                link = "https://animesonlinebr.com.br/video/50035",
                                number = 2
                        )
                ))
    }

    init {
        FactoryChecker.checkFactory(AnimesOnlineBrFactory, VALID_URLS, INVALID_URLS, ESPISODE_AND_SINGLE)
    }

}

