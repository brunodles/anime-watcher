package brunodles.animacurse

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.util.Arrays

@RunWith(Spectrum::class)
class AnimaCurseExplorerTest {

    companion object {
        val VALID_URLS = Arrays.asList("https://animacurse.moe/?p=713", "animacurse.moe?p=123",
                "http://animacurse.moe?p=321")
    }

    init {
        UrlFetcher.useCache = true

        describe("AnimaCurseFactory") {

            describe("when check isEpisode") {

                VALID_URLS.forEach { url ->
                    it("should be able to decode de url \"$url\"") {
                        Assert.assertTrue(AnimaCurseFactory.isEpisode(url))
                    }
                }

                it("should not decode other urls") {
                    Assert.assertFalse(AnimaCurseFactory.isEpisode("animacurse.moe/?cat=123"))
                }
            }

            describe("when episode") {
                val currentEpisode = AnimaCurseFactory.episode("https://animacurse.moe/?p=713")

                describe("when get currentEpisode") {

                    checkEpisode(currentEpisode, Episode(
                            number = 162,
                            description = "Chopper em perigo! Antigo Deus x Sacerdote Shura!",
                            animeName = "One Piece",
                            //                            image = "https://animacurse.moe/imgs/one-piece-episodio-162.webp",
                            link = "https://animacurse.moe/?p=713",
                            video = "https://www.blogger.com/video-play.mp4?contentId=82d054d63f793095"))
                }

                describe("when get nextEpisodes") {

                    val episodes = currentEpisode.nextEpisodes

                    it("should find 4 episodes") {
                        assertEquals(4, episodes.size)
                    }

                    episodeShould(episodes, 0, Episode(
                            number = 163,
                            description = "Sempre Misteriosa! Provação das cordas e provação do amor!?",
                            animeName = "One Piece",
                            image = "https://animacurse.moe/imgs/one-piece-episodio-163.webp",
                            link = "https://animacurse.moe/?p=714"))
                    episodeShould(episodes, 1, Episode(
                            number = 164,
                            description = "Acendam a chama da sabedoria! Wiper, o Guerreiro",
                            animeName = "One Piece",
                            image = "https://animacurse.moe/imgs/one-piece-episodio-164.webp",
                            link = "https://animacurse.moe/?p=715"))
                    episodeShould(episodes, 2, Episode(
                            number = 165,
                            description = "Terra Flutuante de Ouro, Jaya! Para o Santuário de Deus!",
                            animeName = "One Piece",
                            image = "https://animacurse.moe/imgs/one-piece-episodio-165.webp",
                            link = "https://animacurse.moe/?p=716"))
                    episodeShould(episodes, 3, Episode(
                            number = 166,
                            description = "Véspera do Festival do Ouro! Afeta a Vearth",
                            animeName = "One Piece",
                            image = "https://animacurse.moe/imgs/one-piece-episodio-166.webp",
                            link = "https://animacurse.moe/?p=717"))
                }

            }
        }
    }

    private fun episodeShould(episodes: List<Episode>, index: Int, expected: Episode) {
        describe("when get episode at index [$index]") {
            val episode = episodes[index]
            checkEpisode(episode, expected)
        }
    }

    private fun checkEpisode(episode: Episode, expected: Episode) {
        it("should return the correct number") {
            assertEquals(expected.number, episode.number)
        }

        it("should return the correct title") {
            assertEquals(expected.description, episode.description)
        }

        if (expected.image != null)
            it("should return the correct image") {
                assertEquals(expected.image, episode.image)
            }

        if (expected.video != null)
            it("should return the correct video") {
                assertEquals(expected.video, episode.video)
            }

        it("should return the correct animeName") {
            assertEquals(expected.animeName, episode.animeName)
        }

        it("should return the link") {
            assertEquals(expected.link, episode.link)
        }
    }
}