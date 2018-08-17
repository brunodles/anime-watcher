package brunodles.urlfetcher

import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import junit.framework.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class CacheFetcherTest {

    companion object {
        val URLS = mapOf(
                "http://google.com.br" to "google.com",
                "http://www.google.com.br" to "google.com",
                "http://search.google.com.br" to "google.com",
                "http://keep.google.com" to "google.com",
                "http://maps.google.com" to "google.com",

                "https://www.animekaionline.com/tsukipro-the-animation/episodio-1" to "animekaionline.com",
                "https://www.animesonlinebr.com.br/video/50034" to "animesonlinebr.com",
                "http://www.animesorion.video/71672" to "animesorion.video",
                "http://www.animesorion.tv/71672" to "animesorion.tv",
                "http://animesorion.tv" to "animesorion.tv",
                "https://www.animesorion.site/71672" to "animesorion.site",
                "http://animesorion.site" to "animesorion.site",
                "https://animetubebrasil.com/1582/" to "animetubebrasil.com",
                "http://animetubebrasil.com" to "animetubebrasil.com",
                "https://www.anitubebr.com/vd/19249/" to "anitubebr.com",
                "http://anitubebr.com" to "anitubebr.com",
                "https://www.anitube.site/765/" to "anitube.site",
                "http://www.anitube.site" to "anitube.site",
                "https://onepiece-ex.com.br/episodios/online/208/" to "onepiece-ex.com",
                "http://one-piece-x.com.br/episodios/online/207/" to "one-piece-x.com",
                "http://onepiecex.com.br" to "onepiecex.com",
                "http://tamanegi.onepiece-ex.com.br" to "onepiece-ex.com",
                "http://onepiece-ex.com.br" to "onepiece-ex.com",
                "http://tvcurse.com/?p=713" to "tvcurse.com",
                "https://www.xvideos.com/video12026193/anita_troca_o_peluche_pelo_pau" to "xvideos.com",

                "http://tvcurse.com" to "tvcurse.com",
                "http://anitube.tv" to "anitube.tv",
                "http://bagunça.subs.ow.no-ip.com.br" to "no-ip.com"
        )
    }

    init {
        describe("when extractDomain") {
            URLS.forEach { key, value ->
                describe("with $key") {
                    it("should return \"$value\"") {
                        assertEquals(value, CacheFetcher.extractDomain(key))
                    }
                }

            }
        }
    }
}