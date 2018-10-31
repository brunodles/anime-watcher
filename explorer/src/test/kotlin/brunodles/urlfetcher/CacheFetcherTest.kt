package brunodles.urlfetcher

import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class CacheFetcherTest {

    companion object {
        val URLS = mapOf(
            "https://www.animekaionline.com/tsukipro-the-animation/episodio-1" to "animekaionline.com/tsukiprotheanimationepisodio1",
            "https://www.animesonlinebr.com.br/video/50034" to "animesonlinebr.com.br/video50034",
            "http://www.animesorion.video/71672" to "animesorion.video/71672",
            "http://www.animesorion.tv/71672" to "animesorion.tv/71672",
            "https://www.animesorion.site/71672" to "animesorion.site/71672",
            "https://animetubebrasil.com/1582/" to "animetubebrasil.com/1582",
            "https://www.anitubebr.com/vd/19249/" to "anitubebr.com/vd19249",
            "https://www.anitube.site/765/" to "anitube.site/765",
            "https://onepiece-ex.com.br/episodios/online/208/" to "onepieceex.com.br/episodiosonline208",
            "http://one-piece-x.com.br/episodios/online/207/" to "onepiecex.com.br/episodiosonline207",
            "http://tvcurse.com/?p=713" to "tvcurse.com/p713",
            "https://www.xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau" to "xvideos.com/video12026193anita_troca_o_peluche_pelo_pau",

            "http://localhost:8888/redirect300" to "localhost8888/redirect300"
        )
    }

    init {
        describe("when parseToKey") {
            URLS.forEach { url, expectedKey ->
                describe("with $url") {
                    it("should return \"$expectedKey\"") {
                        assertEquals(expectedKey, CacheFetcher.urlToKey(url))
                    }
                }
            }
        }
    }
}
