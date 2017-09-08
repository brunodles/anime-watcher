package brunodles.onepiecex

import brunodles.animewatcher.explorer.AnimeExplorer
import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.*
import org.junit.Assert
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.util.*

@RunWith(Spectrum::class)
class AnimaKaiFactoryTest {

    companion object {

        val VALID_URLS = Arrays.asList(
                "https://www.animakai.info/anime/2019/episodio-419",
                "www.animakai.info/anime/123/episodio-123",
                "animakai.info/anime/2019/episodio-419")
        val INVALID_URLS = Arrays.asList(
                "www.animakai.info/anime/episodio-419",
                "www.animakai.info/anime/2019/")
    }

    init {
        UrlFetcher.useCache = true
        describe("The AnimaKai Factory") {
            val factory = AnimaKaiFactory

            describe("when decode") {

                VALID_URLS.forEach { url ->
                    it("should be able to decode the url \"$url\"") {
                        assertTrue(factory.isEpisode(url))
                    }
                }

                INVALID_URLS.forEach { url ->
                    it("should not decode the invalid url \"$url\"") {
                        Assert.assertFalse(factory.isEpisode(url))
                    }
                }

            }

            describe("when get current episode") {
                val episode = factory.episode(VALID_URLS[0]).currentEpisode
                it("should return video url") {
                    assertEquals("http://www.blogger.com/video-play.mp4?contentId=1e5c9ab13c70079b",
                            episode.video)
                }

                it("should return video description") {
                    assertEquals("One Piece 419", episode.description)
                }
            }

            describe("when get next episodes") {
                val episodes = factory.episode(VALID_URLS[0]).nextEpisodes

                it("should return a list containing one episode") {
                    assertEquals(1, episodes.size)
                }

                it("should return the link to the next episode") {
                    assertEquals("https://www.animakai.info/anime/2019/episodio-420", episodes[0].link)
                }
            }

        }
    }

}
