package brunodles.animewatcher.decoders

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
                video = "https://vid-egc.xvideos-cdn.com/videos/mp4/3/b/8/xvideos.com_3b868ff7bff12466c8ea1fe7b79623ca-1.mp4?ZO04uwlt2FLlkz8q7Xyxk7L6_5H_XrEOh1fTX2L7t8iODJ0VEevLufUG9nVmjU-6_X-c5Rg9CxT_LzX9_tzrKHm5w3p_T211XVW33AgtbuAY1As4iMkSqlGAr-rsXp1gzAR2Ax0FAtar08Ey7tlGaZ7RHt7LbUE6Fl4qVQho93lcNuGBzk-3YUZJiXHRb2_JRS3f0mcITeeRbw",
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
        val useCache = UrlFetcher.useCache
        UrlFetcher.useCache = true
        FactoryChecker.checkFactory(XvideosFactory, VALID_URLS, INVALID_URLS, currentEpisode)
        UrlFetcher.useCache = useCache
    }

}