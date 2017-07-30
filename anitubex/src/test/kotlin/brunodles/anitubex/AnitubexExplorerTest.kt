package brunodles.anitubex

import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnitubexExplorerTest {

    companion object {
        val VIDEO_URL = "http://www.blogger.com/video-play.mp4?contentId=f551984542531bad"
    }

    init {
        UrlFetcher.useCache = true
        UrlFetcher.useLog = true

        describe("The AnitubeX Explorer") {
            val explorer = AnitubexFactory.episode("http://www.anitubex.com/one-piece-785")

            describe("#currentEpisode") {
                val episode = explorer.currentEpisode

                it("should return the episode title") {
                    assertEquals("One Piece 785", episode.description)
                }

                it("should return the  episode video url") {
                    assertEquals(VIDEO_URL, episode.video)
                }
            }

            describe("#nextEpisodes") {

                val episodes = explorer.nextEpisodes

                it("should find 1 episodes") {
                    assertEquals(1, episodes.size)
                }

                it("should return link for the episode") {
                    val single = episodes.single()
                    assertNotNull(single.link)
                    assertNotEquals("", single.link)
                }
            }

        }
    }
}