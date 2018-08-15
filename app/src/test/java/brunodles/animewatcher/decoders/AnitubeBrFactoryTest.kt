package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnitubeBrFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("https://www.anitubebr.com/vd/19249/",
                "https://anitubebr.com/vd/19249/",
                "http://www.anitubebr.com/vd/19249/",
                "http://anitubebr.com/vd/19249/",
                "www.anitubebr.com/vd/19249/",
                "anitubebr.com/vd/19249/"
        )
        val INVALID_URLS = arrayOf("anitub")
        val currentEpisode = Episode(
                number = 1,
                description = "One Piece 01",
                link = "https://anitubebr.com/vd/19249/",
                video = null,
//                video = "blob:https://anitubebr.com/ae38a7f2-22ae-4bd6-9775-8342e70cddd4",
                animeName = "One Piece",
                nextEpisodes = arrayListOf(
                        Episode(number = 2,
                                animeName = "One Piece",
                                description = "Próximo Episódio",
                                link = "https://anitubebr.com/vd/19250/"
                        )
                )
        )
    }

    init {
        FactoryChecker.checkFactory(AnitubeBrFactory, VALID_URLS, INVALID_URLS, currentEpisode)
        Spectrum.xit("Check video validation") {}
    }
}