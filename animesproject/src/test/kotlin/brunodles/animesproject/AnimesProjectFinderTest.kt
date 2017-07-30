package brunodles.animesproject

import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.util.*

@RunWith(Spectrum::class)
class AnimesProjectFinderTest {

    init {

        UrlFetcher.useCache = true
        UrlFetcher.useLog = true

        describe("The AnimesProject finder") {
            val finder = AnimesProjectFinder()

            describe("When find \"one piece\"") {
                val result = finder.search("one piece")

                it("should return a list with 6 items") {
                    assertEquals(6, result.size)
                }

                it("should return items with text") {
                    Arrays.asList(
                            "One Piece",
                            "One Piece Film: Gold",
                            "One Piece: 3D2Y",
                            "One Piece: Adventure of Nebulandia",
                            "One Piece: Episode of Sabo - 3 Kyoudai no Kizuna Kiseki no Saikai to Uketsugareru Ishi",
                            "One Piece: Heart of Gold"
                    ).forEachIndexed { index, s ->
                        assertEquals(s, result[index].title)
                    }
                }

                it("should return the correct episode list") {
                    assertEquals(806, result[0].episodes.size)
                    assertEquals(1, result[1].episodes.size)
                    assertEquals(1, result[2].episodes.size)
                    assertEquals(1, result[3].episodes.size)
                    assertEquals(1, result[4].episodes.size)
                    assertEquals(1, result[5].episodes.size)
                }

                it("should return the link to the first episode") {
                    Arrays.asList(
                            "https://animes.zlx.com.br/exibir/117/14361/one-piece-32",
                            "https://animes.zlx.com.br/exibir/1625/30183/one-piece-film-gold-01",
                            "https://animes.zlx.com.br/serie/o/one-piece-3d2y/713",
                            "https://animes.zlx.com.br/exibir/1345/27237/one-piece-adventure-of-nebulandia-01",
                            "https://animes.zlx.com.br/exibir/1202/24250/one-piece-episode-of-sabo--3-kyoudai-no-kizuna-kiseki-no-saikai-to-uketsugareru-ishi-01",
                            "https://animes.zlx.com.br/exibir/1483/28749/one-piece-heart-of-gold-01"
                    ).forEachIndexed { index, s ->
                        assertEquals(s, result[index].link)
                    }
                }
            }
        }

    }

}