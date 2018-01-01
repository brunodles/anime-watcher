package brunodles.animesonlinebr

import brunodles.animewatcher.explorer.BuildConfig
import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimesOnlineBrFactoryTest {

    companion object {

        val ANIME_NAME = "Boruto: Naruto Next Generations"
        val VALID_URLS = arrayOf(
                "http://www.animesonlinebr.com.br/video/50034")
        val INVALID_URLS = emptyArray<String>()
        val ESPISODE_AND_SINGLE = Episode(
                number = 1,
                description = "Episódio 01",
                animeName = ANIME_NAME,
                image = null,
                link = "http://www.animesonlinebr.com.br/video/500342",
                video = "http://www.blogger.com/video-play.mp4?contentId=3dbfcd746b2b8f21&autoplay=true",
                nextEpisodes = arrayListOf(
                        Episode(animeName = ANIME_NAME,
                                description = "Episódio 02",
                                link = "http://animesonlinebr.com.br/video/50035",
                                number = 2
                        )
                ))
        val ESPISODE_AND_LIST = Episode(
                number = 1,
                description = "Episódio 01",
                animeName = ANIME_NAME,
                image = null,
                link = "http://www.animesonlinebr.com.br/video/50034",
                video = "http://www.blogger.com/video-play.mp4?contentId=3dbfcd746b2b8f21&autoplay=true",
                nextEpisodes = arrayListOf(
                        Episode(number = 2, animeName = ANIME_NAME, description = "Episódio 02", link = "http://www.animesonlinebr.com.br/video/50035"),
                        Episode(number = 3, animeName = ANIME_NAME, description = "Episódio 03", link = "http://www.animesonlinebr.com.br/video/50036"),
                        Episode(number = 4, animeName = ANIME_NAME, description = "Episódio 04", link = "http://www.animesonlinebr.com.br/video/50037"),
                        Episode(number = 5, animeName = ANIME_NAME, description = "Episódio 05", link = "http://www.animesonlinebr.com.br/video/50038"),
                        Episode(number = 6, animeName = ANIME_NAME, description = "Episódio 06", link = "http://www.animesonlinebr.com.br/video/50039"),
                        Episode(number = 7, animeName = ANIME_NAME, description = "Episódio 07", link = "http://www.animesonlinebr.com.br/video/50040"),
                        Episode(number = 8, animeName = ANIME_NAME, description = "Episódio 08", link = "http://www.animesonlinebr.com.br/video/50041"),
                        Episode(number = 9, animeName = ANIME_NAME, description = "Episódio 09", link = "http://www.animesonlinebr.com.br/video/50042"),
                        Episode(number = 10, animeName = ANIME_NAME, description = "Episódio 10", link = "http://www.animesonlinebr.com.br/video/50043"),
                        Episode(number = 11, animeName = ANIME_NAME, description = "Episódio 11", link = "http://www.animesonlinebr.com.br/video/50044"),
                        Episode(number = 12, animeName = ANIME_NAME, description = "Episódio 12", link = "http://www.animesonlinebr.com.br/video/50045"),
                        Episode(number = 13, animeName = ANIME_NAME, description = "Episódio 13", link = "http://www.animesonlinebr.com.br/video/50046"),
                        Episode(number = 14, animeName = ANIME_NAME, description = "Episódio 14", link = "http://www.animesonlinebr.com.br/video/50359"),
                        Episode(number = 15, animeName = ANIME_NAME, description = "Episódio 15", link = "http://www.animesonlinebr.com.br/video/50431"),
                        Episode(number = 16, animeName = ANIME_NAME, description = "Episódio 16", link = "http://www.animesonlinebr.com.br/video/50557"),
                        Episode(number = 17, animeName = ANIME_NAME, description = "Episódio 17", link = "http://www.animesonlinebr.com.br/video/54137"),
                        Episode(number = 18, animeName = ANIME_NAME, description = "Episódio 18", link = "http://www.animesonlinebr.com.br/video/55140"),
                        Episode(number = 19, animeName = ANIME_NAME, description = "Episódio 19", link = "http://www.animesonlinebr.com.br/video/57350"),
                        Episode(number = 20, animeName = ANIME_NAME, description = "Episódio 20", link = "http://www.animesonlinebr.com.br/video/57456"),
                        Episode(number = 2, animeName = ANIME_NAME, description = "OVA 02", link = "http://www.animesonlinebr.com.br/video/58315"),
                        Episode(number = 21, animeName = ANIME_NAME, description = "Episódio 21", link = "http://www.animesonlinebr.com.br/video/58324"),
                        Episode(number = 22, animeName = ANIME_NAME, description = "Episódio 22", link = "http://www.animesonlinebr.com.br/video/59748"),
                        Episode(number = 23, animeName = ANIME_NAME, description = "Episódio 23", link = "http://www.animesonlinebr.com.br/video/60510"),
                        Episode(number = 24, animeName = ANIME_NAME, description = "Episódio 24", link = "http://www.animesonlinebr.com.br/video/61036"),
                        Episode(number = 25, animeName = ANIME_NAME, description = "Episódio 25", link = "http://www.animesonlinebr.com.br/video/62182"),
                        Episode(number = 26, animeName = ANIME_NAME, description = "Episódio 26", link = "http://www.animesonlinebr.com.br/video/62924"),
                        Episode(number = 27, animeName = ANIME_NAME, description = "Episódio 27", link = "http://www.animesonlinebr.com.br/video/63555"),
                        Episode(number = 28, animeName = ANIME_NAME, description = "Episódio 28", link = "http://www.animesonlinebr.com.br/video/64451"),
                        Episode(number = 29, animeName = ANIME_NAME, description = "Episódio 29", link = "http://www.animesonlinebr.com.br/video/64942"),
                        Episode(number = 30, animeName = ANIME_NAME, description = "Episódio 30", link = "http://www.animesonlinebr.com.br/video/65024"),
                        Episode(number = 31, animeName = ANIME_NAME, description = "Episódio 31", link = "http://www.animesonlinebr.com.br/video/65106"),
                        Episode(number = 32, animeName = ANIME_NAME, description = "Episódio 32", link = "http://www.animesonlinebr.com.br/video/66903"),
                        Episode(number = 33, animeName = ANIME_NAME, description = "Episódio 33", link = "http://www.animesonlinebr.com.br/video/67326"),
                        Episode(number = 34, animeName = ANIME_NAME, description = "Episódio 34", link = "http://www.animesonlinebr.com.br/video/67399"),
                        Episode(number = 35, animeName = ANIME_NAME, description = "Episódio 35", link = "http://www.animesonlinebr.com.br/video/67885"),
                        Episode(number = 36, animeName = ANIME_NAME, description = "Episódio 36", link = "http://www.animesonlinebr.com.br/video/68044"),
                        Episode(number = 37, animeName = ANIME_NAME, description = "Episódio 37", link = "http://www.animesonlinebr.com.br/video/68115"),
                        Episode(number = 38, animeName = ANIME_NAME, description = "Episódio 38", link = "http://www.animesonlinebr.com.br/video/68187"),
                        Episode(number = 39, animeName = ANIME_NAME, description = "Episódio 39", link = "http://www.animesonlinebr.com.br/video/68318")
                )
        )
    }

    init {
        FactoryChecker.checkFactory(AnimesOnlineBrFactory, VALID_URLS, INVALID_URLS, ESPISODE_AND_LIST)
        @Suppress("ConstantConditionIf")
        if (BuildConfig.USE_CACHE)
            FactoryChecker.checkFactory(AnimesOnlineBrFactory,
                    arrayOf("http://www.animesonlinebr.com.br/video/500342"), emptyArray(),
                    ESPISODE_AND_SINGLE)
    }

}

