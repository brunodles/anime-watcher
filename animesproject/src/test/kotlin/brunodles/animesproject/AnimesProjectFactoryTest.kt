package brunodles.animesproject

import brunodles.animewatcher.explorer.AnimeExplorer
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.runner.RunWith
import java.util.*

@RunWith(Spectrum::class)
class AnimesProjectFactoryTest {

    companion object {
        val VALID_URLS = Arrays.asList("https://animes.zlx.com.br/exibir/117/3440/one-piece-166",
                "http://animes.zlx.com.br/exibir/117/3440/one-piece-166",
                "animes.zlx.com.br/exibir/123/321")
    }

    init {

        describe("The AnimesProject Factory") {
            val factory = AnimesProjectFactory

            VALID_URLS.forEach { url ->
                it("should be able to decode de url \"$url\"") {
                    Assert.assertTrue(factory.isEpisode(url))
                }
            }

            it("should not decode invalid url") {
                Assert.assertFalse(factory.isEpisode("animes.zlx.com.br"))
            }

            it("should return a Explorer") {
                Assert.assertTrue(factory.episode(VALID_URLS[0]) is AnimeExplorer)
            }
        }
    }

}