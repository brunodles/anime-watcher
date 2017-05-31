package brunodles.anitubex

import bruno.animewatcher.explorer.AnimeExplorer
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.runner.RunWith
import java.util.*

@RunWith(Spectrum::class)
class AnitubexFactoryTest {

    companion object {
        val VALID_URLS = Arrays.asList("http://www.anitubex.com/one-piece-1",
                "https://www.anitubex.com/one-piece-1")
    }

    init {

        describe("The AnimesProject Factory") {
            val factory = AnitubexFactory

            VALID_URLS.forEach { url ->
                it("should be able to decode de url \"$url\"") {
                    Assert.assertTrue(factory.isEpisode(url))
                }
            }

            it("should not decode invalid url") {
                Assert.assertFalse(factory.isEpisode("anitube.jp/episode"))
            }

            it("should return a Explorer") {
                Assert.assertTrue(factory.episode(VALID_URLS[0]) is AnimeExplorer)
            }
        }
    }

}