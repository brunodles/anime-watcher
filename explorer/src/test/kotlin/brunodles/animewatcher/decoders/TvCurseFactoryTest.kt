package brunodles.animewatcher.decoders

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.testhelper.FactoryChecker
import brunodles.animewatcher.testhelper.FactoryChecker.whenCheckIsEpisode
import brunodles.animewatcher.testhelper.FactoryChecker.whenEpisode
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class TvCurseFactoryTest {

    companion object {
        val VALID_URLS = arrayOf(
            "https://tvcurse.com/?p=713",
            "http://tvcurse.com/?p=713",
            "tvcurse.com?p=123",
            "http://tvcurse.com?p=321",
            "animacurse.moe/?p=713",
            "animacurse.tv/?p=713"
        )
        val INVALID_URLS = arrayOf("tvcurse.com/?cat=123")
        val PLAYER_WITH_NEXT = Episode(
            number = 162,
            description = "Assistir One Piece - Episódio 162 – Chopper em perigo! Antigo Deus x Sacerdote Shura! Online",
            animeName = "One Piece",
            image = "https://tvcurse.com/imgs/one-piece-episodio-162.webp",
            link = "https://tvcurse.com/?p=713",
            video = "https://tvcurse.com/dd/ODJkMDU0ZDYzZjc5MzA5NQ==",
            nextEpisodes = arrayListOf(
                Episode(
                    number = 163,
                    description = "Episódio 163 – Sempre Misteriosa! Provação das cordas e provação do amor!?",
                    animeName = "One Piece",
                    image = "https://tvcurse.com/imgs/one-piece-episodio-163.webp",
                    link = "https://tvcurse.com/?p=714"
                ),
                Episode(
                    number = 164,
                    description = "Episódio 164 – Acendam a chama da sabedoria! Wiper, o Guerreiro",
                    animeName = "One Piece",
                    image = "https://tvcurse.com/imgs/one-piece-episodio-164.webp",
                    link = "https://tvcurse.com/?p=715"
                ),
                Episode(
                    number = 165,
                    description = "Episódio 165 – Terra Flutuante de Ouro, Jaya! Para o Santuário de Deus!",
                    animeName = "One Piece",
                    image = "https://tvcurse.com/imgs/one-piece-episodio-165.webp",
                    link = "https://tvcurse.com/?p=716"
                ),
                Episode(
                    number = 166,
                    description = "Episódio 166 – Véspera do Festival do Ouro! Afeta a Vearth",
                    animeName = "One Piece",
                    image = "https://tvcurse.com/imgs/one-piece-episodio-166.webp",
                    link = "https://tvcurse.com/?p=717"
                )
            )
        )
        val PLAYER_WITHOUT_NEXT = Episode(
            number = 11,
            description = "Assistir Himouto! Umaru-chan ova 11 – Especial ova 11 Online",
            animeName = "Himouto! Umaru-chan",
            image = "https://tvcurse.com/imgs/himouto-umaru-chan-ova-11.webp",
            link = "https://tvcurse.com/?p=43449",
            video = "https://tvcurse.com/dd/YzRjMjhmMDc4MGY5NzExNg==",
            nextEpisodes = emptyList()
        )
    }

    init {
        FactoryChecker.describe(TvCurseFactory) {
            describe("when page contains next episodes") {
                whenEpisode(PLAYER_WITH_NEXT)
            }
            describe("when page does not contains next") {
                whenEpisode(PLAYER_WITHOUT_NEXT)
            }
            whenCheckIsEpisode(VALID_URLS, INVALID_URLS)
        }
    }
}