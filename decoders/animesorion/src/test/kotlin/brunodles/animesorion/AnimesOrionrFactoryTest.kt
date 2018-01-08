package brunodles.animesorion

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimesOrionrFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("http://www.animesorion.tv/71672")
        val INVALID_URLS = arrayOf("animacurse.moe/?cat=123")
        val SINGLE_NEXT_EPISODES = Episode(
                number = 2,
                description = "Episódio 2",
                animeName = "Boku No Hero Academia",
                image = null,
                link = "http://www.animesorion.tv/71672s",
                video = "https://www.blogger.com/video-play.mp4?contentId=b0bccad36e2be8e1",
                nextEpisodes = arrayListOf(
                        Episode(number = 3,
                                description = "Episódio 3",
                                animeName = "Boku No Hero Academia",
                                image = null,
                                link = "http://www.animesorion.tv/71808")
                )
        )
        val NO_NEXT_EPISODES = Episode(
                number = 12,
                description = "Episódio 12",
                animeName = "Himouto! Umaru-chan",
                image = null,
                link = "http://www.animesorion.tv/51555",
                video = "https://www.blogger.com/video-play.mp4?contentId=e4e3e680e0b6ed74",
                nextEpisodes = emptyList()
        )

        val ABOUTPAGE_EPISODES = Episode(
                number = 1,
                description = "Episódio 1",
                animeName = "Boku No Hero Academia 2",
                image = null,
                link = "http://www.animesorion.tv/81318",
                video = "https://www.blogger.com/video-play.mp4?contentId=59a70b30c1bf47a0",
                nextEpisodes = arrayListOf(
                        Episode(number=2, description="Episódio 2", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/81518"),
                        Episode(number=3, description="Episódio 3", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/81679"),
                        Episode(number=4, description="Episódio 4", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/81926"),
                        Episode(number=5, description="Episódio 5", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/82143"),
                        Episode(number=6, description="Episódio 6", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/82162"),
                        Episode(number=7, description="Episódio 7", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/82459"),
                        Episode(number=8, description="Episódio 8", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/84237"),
                        Episode(number=9, description="Episódio 9", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/93021"),
                        Episode(number=10, description="Episódio 10", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/105627"),
                        Episode(number=11, description="Episódio 11", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/111799"),
                        Episode(number=12, description="Episódio 12", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/112034"),
                        Episode(number=13, description="Episódio 13", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/112396"),
                        Episode(number=14, description="Episódio 14", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/112524"),
                        Episode(number=15, description="Episódio 15", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/113447"),
                        Episode(number=16, description="Episódio 16", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/113752"),
                        Episode(number=17, description="Episódio 17", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/113881"),
                        Episode(number=18, description="Episódio 18", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/114039"),
                        Episode(number=19, description="Episódio 19", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/114191"),
                        Episode(number=20, description="Episódio 20", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/114346"),
                        Episode(number=21, description="Episódio 21", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/114525"),
                        Episode(number=22, description="Episódio 22", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/114873"),
                        Episode(number=23, description="Episódio 23", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/115039"),
                        Episode(number=24, description="Episódio 24", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/115218"),
                        Episode(number=25, description="Episódio 25", animeName="Boku No Hero Academia 2", link="http://www.animesorion.tv/115418")
                )
        )
    }

    init {
        FactoryChecker.checkFactory(AnimesOrionFactory, VALID_URLS, INVALID_URLS, SINGLE_NEXT_EPISODES)
        FactoryChecker.checkFactory(AnimesOrionFactory, expectedEpisode = ABOUTPAGE_EPISODES)
        FactoryChecker.checkFactory(AnimesOrionFactory, expectedEpisode = NO_NEXT_EPISODES)
    }
}