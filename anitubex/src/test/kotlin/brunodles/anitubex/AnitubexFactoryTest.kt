package brunodles.anitubex

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.UrlFetcher
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.util.Arrays

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
                                description = "Próximo Episódio",
                                link = "http://www.anitubex.com/one-piece-2")
                )
        )
    }

    init {
        UrlFetcher.useCache = true
        FactoryChecker.checkFactory(AnitubexFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }
}