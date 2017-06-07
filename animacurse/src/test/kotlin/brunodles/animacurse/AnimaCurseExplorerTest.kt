package brunodles.animacurse

import bruno.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.util.regex.Pattern

@RunWith(Spectrum::class)
class AnimaCurseExplorerTest {

    companion object {
        val EPISODE_DESCRIPTION = "Episódio 162 – Chopper em perigo! Antigo Deus x Sacerdote Shura!"
        val EPISODE_VIDEO_URL = "https://www.blogger.com/video-play.mp4?contentId=82d054d63f793095"
    }

    init {
        UrlFetcher.useCache = true

        describe("The AnimaCurse Explorer") {
            val explorer = AnimaCurseFactory.episode("https://animacurse.moe/?p=713")

            describe("#currentEpisode") {
                val episode = explorer.currentEpisode

                it("should return the episode title") {
                    assertEquals(EPISODE_DESCRIPTION, episode.description)
                }

                it("should return the  episode video url") {
                    assertEquals(EPISODE_VIDEO_URL, episode.video)
                }
            }

            describe("#nextEpisodes") {

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
            }

        }
    }
}