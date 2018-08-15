package brunodles.animewatcher.decoders

import brunodles.animacurse.XvideosFactory
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.urlfetcher.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class XvideosFactoryTest {

    private companion object {
        val VALID_URLS = arrayOf("https://www.xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau",
                "http://www.xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau",
                "xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau")
        val INVALID_URLS = emptyArray<String>()
        val currentEpisode = Episode(
                number = 1,
                description = "Anita Troca O Peluche Pelo Pau",
                animeName = "Anita Troca O Peluche Pelo Pau",
                image = null,
                link = "https://www.xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau",
                video = "https://video-hw.xvideos-cdn.com/videos/mp4/3/b/8/xvideos.com_3b868ff7bff12466c8ea1fe7b79623ca-1.mp4?e=1516254528&ri=1024&rs=85&h=bb0332cc71ae04e3301d5e3c23fdbe68",
                nextEpisodes = arrayListOf(
                        Episode(number = 2,
                                animeName = "A Real Slut In Real Estate",
                                image = "https://img-egc.xvideos-cdn.com/videos/thumbs169/40/4a/72/404a7276766fc9256d81090c97cac919/404a7276766fc9256d81090c97cac919.5.jpg",
                                description = "13 min",
                                link = "http://xvideos.com/video32232811/a_real_slut_in_real_estate")
                ),
                temporaryVideoUrl = false
        )
    }

    init {
        UrlFetcher.useCache = true
        FactoryChecker.checkFactory(XvideosFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }

}