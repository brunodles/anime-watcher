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
            "http://google.com.br" to "google.com.br/_index",
            "http://www.google.com.br" to "google.com.br/_index",
            "http://search.google.com.br" to "search.google.com.br/_index",
            "http://keep.google.com" to "keep.google.com/_index",
            "http://maps.google.com" to "maps.google.com/_index",

            "https://www.animekaionline.com/tsukipro-the-animation/episodio-1" to "animekaionline.com/tsukiprotheanimationepisodio1",
            "https://www.animesonlinebr.com.br/video/50034" to "animesonlinebr.com.br/video50034",

            "http://www.animesorion.video/71672" to "animesorion.video/71672",
            "http://www.animesorion.tv/71672" to "animesorion.tv/71672",
            "http://animesorion.tv" to "animesorion.tv/_index",
            "https://www.animesorion.site/71672" to "animesorion.site/71672",
            "http://animesorion.site" to "animesorion.site/_index",

            "https://animetubebrasil.com/1582/" to "animetubebrasil.com/1582",
            "http://animetubebrasil.com" to "animetubebrasil.com/_index",

            "https://www.anitubebr.com/vd/19249/" to "anitubebr.com/vd19249",
            "http://anitubebr.com" to "anitubebr.com/_index",

            "https://www.anitube.site/765/" to "anitube.site/765",
            "http://www.anitube.site" to "anitube.site/_index",
            "http://anitube.tv" to "anitube.tv/_index",

            "https://onepiece-ex.com.br/episodios/online/208/" to "onepieceex.com.br/episodiosonline208",
            "http://one-piece-x.com.br/episodios/online/207/" to "onepiecex.com.br/episodiosonline207",
            "http://onepiecex.com.br" to "onepiecex.com.br/_index",
            "http://tamanegi.onepiece-ex.com.br" to "tamanegi.onepieceex.com.br/_index",
            "http://onepiece-ex.com.br" to "onepieceex.com.br/_index",

            "http://tvcurse.com/?p=713" to "tvcurse.com/p713",
            "http://tvcurse.com" to "tvcurse.com/_index",

            "https://www.xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau" to "xvideos.com/video12026193anita_troca_o_peluche_pelo_pau",

            "http://bagunca.subs.ow.no-ip.com.br" to "bagunca.subs.ow.noip.com.br/_index",

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
