package brunodles.anitubex

import bruno.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnitubexFinderTest {

    init {

        UrlFetcher.useCache = true

        describe("The AnitubeX finder") {
            val finder = AnitubexFinder()

            describe("When find \"one piece\"") {
                val result = finder.search("one piece")

                it("should return a list with 2 items") {
                    assertEquals(2, result.size)
                }

                it("should return itens with text") {
                    assertEquals("Especial One Piece Heart of Gold", result[0].title)
                    assertEquals("One Piece", result[1].title)
                }

                it("should return the correct episode list") {
                    assertEquals(1, result[0].episodes.size)
                    assertEquals(790, result[1].episodes.size)
                }
            }
        }

    }

}