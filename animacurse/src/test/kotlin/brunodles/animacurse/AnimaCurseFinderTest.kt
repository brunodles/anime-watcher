package brunodles.animacurse

import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class AnimaCurseFinderTest {

    init {

        UrlFetcher.useCache = true
        UrlFetcher.useLog = true

        describe("The AnimaCurse finder") {
            val finder = AnimaCurseFinder()

            describe("When find \"one piece\"") {
                val result = finder.search("one piece")

                it("should return a list with 2 items") {
                    Assert.assertEquals(2, result.size)
                }

                it("should return items with text") {
                    Assert.assertEquals("One Piece", result[0].title)
                    Assert.assertEquals("One Piece Dublado", result[1].title)
                }

                it("should return the correct episode list") {
                    Assert.assertEquals(12, result[0].episodes.size)
                    Assert.assertEquals(12, result[1].episodes.size)
                }

                it("should return the link to the first episode") {
                    Assert.assertEquals("https://animacurse.moe/?p=553", result[0].link)
                    Assert.assertEquals("https://animacurse.moe/?p=1262", result[1].link)
                }
            }
        }

    }

}