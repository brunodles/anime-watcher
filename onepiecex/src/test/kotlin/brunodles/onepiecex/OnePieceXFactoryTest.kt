package brunodles.onepiecex

import bruno.animewatcher.explorer.*
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.*
import org.jsoup.nodes.Document
import org.junit.Assert
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
        describe("The OnePieceX Factory") {
            val factory = OnePieceXFactory

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

            it("should return a Explorer") {
                Assert.assertTrue(factory.episode(VALID_URLS[0]) is AnimeExplorer)
            }

        }
    }

}
