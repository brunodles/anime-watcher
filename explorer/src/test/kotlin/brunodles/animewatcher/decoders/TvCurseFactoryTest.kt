package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import com.greghaskins.spectrum.Spectrum
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class TvCurseFactoryTest {


    companion object {
        val VALID_URLS = arrayOf("https://tvcurse.com/?p=713", "https://tvcurse.com/?p=713",
                "tvcurse.com?p=123", "http://tvcurse.com?p=321")
        val INVALID_URLS = arrayOf("tvcurse.com/?cat=123")
        val currentEpisode = Episode(
                number = 162,
                description = "Chopper em perigo! Antigo Deus x Sacerdote Shura!",
                animeName = "One Piece",
                image = "https://tvcurse.com/imgs/one-piece-episodio-162.webp",
                link = "https://tvcurse.com/?p=713",
                video = "https://tvcurse.com/dd/ODJkMDU0ZDYzZjc5MzA5NQ==",
                nextEpisodes = arrayListOf(
                        Episode(number = 163,
                                description = "Sempre Misteriosa! Provação das cordas e provação do amor!?",
                                animeName = "One Piece",
                                image = "https://tvcurse.com/imgs/one-piece-episodio-163.webp",
                                link = "https://tvcurse.com/?p=714"),
                        Episode(number = 164,
                                description = "Acendam a chama da sabedoria! Wiper, o Guerreiro",
                                animeName = "One Piece",
                                image = "https://tvcurse.com/imgs/one-piece-episodio-164.webp",
                                link = "https://tvcurse.com/?p=715"),
                        Episode(number = 165,
                                description = "Terra Flutuante de Ouro, Jaya! Para o Santuário de Deus!",
                                animeName = "One Piece",
                                image = "https://tvcurse.com/imgs/one-piece-episodio-165.webp",
                                link = "https://tvcurse.com/?p=716"),
                        Episode(number = 166,
                                description = "Véspera do Festival do Ouro! Afeta a Vearth",
                                animeName = "One Piece",
                                image = "https://tvcurse.com/imgs/one-piece-episodio-166.webp",
                                link = "https://tvcurse.com/?p=717")
                )
        )
    }

    init {
        FactoryChecker.checkFactory(TvCurseFactory, VALID_URLS, INVALID_URLS, currentEpisode)
    }
}