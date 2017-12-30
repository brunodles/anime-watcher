package brunodles.animesproject

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
class AnimesProjectFactoryTest {

    companion object {
        val VALID_URLS = arrayOf("https://animes.zlx.com.br/exibir/117/3440/one-piece-166")
        val INVALID_URLS = arrayOf("https://animes.zlx.com.br")
        val currentEpisode = Episode(
                number = 166,
                description = "Episódio 166",
                animeName = "One Piece",
                image = null,
                link = "https://animes.zlx.com.br/exibir/117/3440/one-piece-166",
                video = "https://st01hd.animesproject.com.br/download/.*?/O/one-piece/MQ/episodios/166.mp4",
//                video = "https://st01hd.animesproject.com.br/download/AKfxmXjYdwaDAgpvRdr2SA/1511216870/O/one-piece/MQ/episodios/166.mp4",
                nextEpisodes = arrayListOf(
                        Episode(number = 167,
                                description = "Episódio 167",
                                link = "http://animes.zlx.com.br/exibir/117/3414/one-piece-167"),
                        Episode(number = 168,
                                description = "Episódio 168",
                                link = "http://animes.zlx.com.br/exibir/117/3441/one-piece-168")
                )
        )
    }

    init {
        UrlFetcher.useCache = true
        FactoryChecker.checkFactory(AnimesProjectFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }
}