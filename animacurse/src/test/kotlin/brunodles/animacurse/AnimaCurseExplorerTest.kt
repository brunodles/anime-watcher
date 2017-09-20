package brunodles.animacurse

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.runner.RunWith
import java.util.Arrays
import java.util.regex.Pattern

@RunWith(Spectrum::class)
class AnimaCurseExplorerTest {

    companion object {
        val EPISODE_DESCRIPTION = "Episódio 162 – Chopper em perigo! Antigo Deus x Sacerdote Shura!"
        val EPISODE_VIDEO_URL = "https://www.blogger.com/video-play.mp4?contentId=82d054d63f793095"
        val VALID_URLS = Arrays.asList("https://animacurse.moe/?p=713", "animacurse.moe?p=123",
                "http://animacurse.moe?p=321")
    }

    init {
        UrlFetcher.useCache = true

        describe("AnimaCurseFactory") {

            VALID_URLS.forEach { url ->
                it("should be able to decode de url \"$url\"") {
                    Assert.assertTrue(AnimaCurseFactory.isEpisode(url))
                }
            }

            it("should not decode other urls") {
                Assert.assertFalse(AnimaCurseFactory.isEpisode("animacurse.moe/?cat=123"))
            }
        }

        describe("The AnimaCurse Explorer") {
            val explorer = AnimaCurseFactory.episode("https://animacurse.moe/?p=713")

            describe("when currentEpisode") {
                val episode = explorer.currentEpisode

                it("should return the episode title") {
                    assertEquals(EPISODE_DESCRIPTION, episode.description)
                }

                it("should return the  episode video url") {
                    assertEquals(EPISODE_VIDEO_URL, episode.video)
                }
            }

            describe("when nextEpisodes") {

                val episodes = explorer.nextEpisodes

                it("should find 4 episodes") {
                    assertEquals(4, episodes.size)
                }

                it("should return the episodes in the right order") {
                    val numberPattern = Pattern.compile("\\d+")
                    val expectedOrder: IntArray = intArrayOf(163, 164, 165, 166)
                    val order = episodes
                            .map {
                                val matcher = numberPattern.matcher(it.description)
                                if (matcher.find())
                                    Integer.valueOf(matcher.group())
                                else
                                    0
                            }
                            .toIntArray()
                    assertArrayEquals(expectedOrder, order)
                }

                it("should return links for all episodes") {
                    episodes.forEach {
                        assertNotNull(it.link)
                        assertNotEquals("", it.link)
                    }
                }

                episodeShould(episodes, 0,
                        "Episódio 163 – Sempre Misteriosa! Provação das cordas e provação do amor!?",
                        "https://animacurse.moe/imgs/one-piece-episodio-163.webp",
                        "https://animacurse.moe/?p=714")
                episodeShould(episodes, 1,
                        "Episódio 164 – Acendam a chama da sabedoria! Wiper, o Guerreiro",
                        "https://animacurse.moe/imgs/one-piece-episodio-164.webp",
                        "https://animacurse.moe/?p=715")
                episodeShould(episodes, 2,
                        "Episódio 165 – Terra Flutuante de Ouro, Jaya! Para o Santuário de Deus!",
                        "https://animacurse.moe/imgs/one-piece-episodio-165.webp",
                        "https://animacurse.moe/?p=716")
                episodeShould(episodes, 3,
                        "Episódio 166 – Véspera do Festival do Ouro! Afeta a Vearth",
                        "https://animacurse.moe/imgs/one-piece-episodio-166.webp",
                        "https://animacurse.moe/?p=717")
            }

        }
    }

    private fun episodeShould(episodes: List<Episode>, index: Int, title: String, imageUrl: String, link: String) {
        describe("when get episode at index [$index]") {
            val episode = episodes[index]

            it("should return the correct title") {
                assertEquals(title, episode.description)
            }

            it("should return the correct image") {
                assertEquals(imageUrl, episode.image)
            }

            it("should return the link") {
                assertEquals(link, episode.link)
            }
        }
    }
}