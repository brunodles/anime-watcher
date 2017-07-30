package brunodles.onepiecex

import brunodles.animewatcher.explorer.AnimeExplorer
import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.util.*

@RunWith(Spectrum::class)
class OnePieceXFactoryTest {

    companion object {

        val VALID_URLS = Arrays.asList(
                "https://one-piece-x.com.br/episodios/online/208/",
                "http://one-piece-x.com.br/episodios/online/207/",
                "one-piece-x.com.br/episodios/online/209")
        val INVALID_URLS = Arrays.asList(
                "https://one-piece-x.com.br/episodios/t04/",
                "one-piece-x.com.br/episodios/online")
    }

    init {
        UrlFetcher.useCache = true
        describe("The OnePieceX Factory") {
            val factory = OnePieceXFactory

            describe("when decode") {

                VALID_URLS.forEach { url ->
                    it("should be able to decode the url \"$url\"") {
                        assertTrue(factory.isEpisode(url))
                    }
                }

                INVALID_URLS.forEach { url ->
                    it("should not decode the invalid url \"$url\"") {
                        assertFalse(factory.isEpisode(url))
                    }
                }

                it("should return a Explorer") {
                    assertTrue(factory.episode(VALID_URLS[0]) is AnimeExplorer)
                }
            }

            describe("when get current episode") {
                val episode = factory.episode(VALID_URLS[0]).currentEpisode

                it("should return video url") {
                    assertEquals("http://klahadorv2.onepieceex.com.br/episodios/online/OpEx_208_online.webm?st=xNdWjVPClaciG4li0VmWIg&e=1499617180", episode.video)
                }

                it("should return video description") {
                    assertEquals("Episódio 208: Os piratas do Foxy!! A Davy Back!", episode.description)
                }

            }

            describe("when get next episodes") {
                val episodes = factory.episode(VALID_URLS[0]).nextEpisodes

                it("should return a list containing 700 episodes") {
                    assertEquals(797, episodes.size)
                }

                it("should return episodes with a link") {
                    for (episode in episodes) {
                        assertTrue(!episode.link.isNullOrEmpty())
                    }
                }

            }

        }
    }

}
