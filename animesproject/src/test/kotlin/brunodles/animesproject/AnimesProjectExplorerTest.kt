package brunodles.animesproject

import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.util.regex.Pattern


@RunWith(Spectrum::class)
class AnimesProjectExplorerTest {

    companion object {
        val EPISODE_VIDEO_URL = "https://st01hd.animesproject.com.br/download/VzpwRSRCb7FECT28lQHH3A/1496029248/O/one-piece/MQ/episodios/166.mp4"
    }

    init {
        UrlFetcher.useCache = true

        describe("The AnimesProject Explorer") {
            val explorer = AnimesProjectFactory.episode("https://animes.zlx.com.br/exibir/117/3440/one-piece-166")

            describe("when currentEpisode") {
                val currentEpisode = explorer.currentEpisode

                it("should return the episode title") {
                    assertEquals("One Piece : Episódio 166", currentEpisode.description)
                }

                it("should return the episode video url") {
                    assertEquals(EPISODE_VIDEO_URL, currentEpisode.video)
                }
            }

            describe("when nextEpisodes") {
                val episodes = explorer.nextEpisodes

                it("should find 2 episodes") {
                    assertEquals(2, episodes.size)
                }

                it("should return the episodes in the right order") {
                    val numberPattern = Pattern.compile("\\d+")
                    val expectedOrder = intArrayOf(167, 168)
                    val order = episodes.map {
                        val matcher = numberPattern.matcher(it.description)
                        if (matcher.find())
                            Integer.valueOf(matcher.group())
                        else
                            0
                    }.toIntArray()
                    Assert.assertArrayEquals(expectedOrder, order)
                }

                it("should return links for all episodes") {
                    episodes.forEach {
                        Assert.assertNotNull(it.link)
                        Assert.assertNotEquals("", it.link)
                    }
                }
            }
        }
    }
}