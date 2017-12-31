package brunodles.onepiecex

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimaKaiFactoryTest {

    companion object {

        val VALID_URLS = arrayOf(
                "https://www.animekaionline.com/tsukipro-the-animation/episodio-1")
        val INVALID_URLS = arrayOf(
                "www.animakai.info/anime/episodio-419",
                "www.animakai.info/anime/2019/")
        val currentEpisode = Episode(
                number = 1,
                description = "Tsukipro The Animation 1",
                animeName = "Tsukipro The Animation",
                image = "http://www.animekai.info/2017/10/Screenshot_33.jpg",
                link = "https://www.animekaionline.com/tsukipro-the-animation/episodio-1",
                //                video = "http://www.blogger.com/video-play.mp4?contentId=1e5c9ab13c70079b",
                video = "https://www.blogger.com/video-play.mp4?contentId=b186c220e9973f58",
                nextEpisodes = arrayListOf()
        )
    }

    init {
        FactoryChecker.checkFactory(AnimaKaiFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }

}
