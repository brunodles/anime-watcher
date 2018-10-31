package brunodles.urlfetcher

import brunodles.animewatcher.explorer.BuildConfig
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlMatching
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.google.common.net.HttpHeaders.LOCATION
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.afterAll
import com.greghaskins.spectrum.Spectrum.beforeAll
import com.greghaskins.spectrum.Spectrum.beforeEach
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.jsoup.nodes.Document
import org.junit.Assert
import org.junit.runner.RunWith

@RunWith(Spectrum::class)
class UrlFetcherTest {
    val httpPort = 8888
    val host = "http://localhost:$httpPort"
    val wireMockServer = WireMockServer(options().port(httpPort).httpsPort(httpPort + 1))

    init {
        beforeAll {
            UrlFetcher.cacheDir = "${BuildConfig.ROOT_DIR}/explorer"
            wireMockServer.start()
        }
        afterAll {
            wireMockServer.resetAll()
            wireMockServer.stop()
            UrlFetcher.cacheDir = BuildConfig.ROOT_DIR
        }

        describe("UrlFetcher") {
            describe("with cache") {
                val fetcher: (String) -> Document = { url ->
                    UrlFetcher.composableFetcher()
                        .withCache()
                        .withJsRedirect()
                        .get(url)
                }
                describe("when page have http redirects") {
                    shouldFollowRedirectWithStatusCode(fetcher, 300)
                    shouldFollowRedirectWithStatusCode(fetcher, 301)
                    shouldFollowRedirectWithStatusCode(fetcher, 302)
                    shouldFollowRedirectWithStatusCode(fetcher, 303)
                    shouldFollowRedirectWithStatusCode(fetcher, 307)
                    shouldFollowRedirectWithStatusCode(fetcher, 308)
                }
                shouldFollowRedirectWith(fetcher, "Html") { htmlRedirect(it) }
                shouldFollowRedirectWith(fetcher, "Js") { jsRedirect(it) }
            }
            describe("without cache") {
                val fetcher: (String) -> Document = { url ->
                    UrlFetcher.composableFetcher()
                        .withJsRedirect()
                        .get(url)
                }
                describe("when page have html redirects") {
                    shouldFollowRedirectWithStatusCode(fetcher, 300)
                    shouldFollowRedirectWithStatusCode(fetcher, 301)
                    shouldFollowRedirectWithStatusCode(fetcher, 302)
                    shouldFollowRedirectWithStatusCode(fetcher, 303)
                    shouldFollowRedirectWithStatusCode(fetcher, 307)
                    shouldFollowRedirectWithStatusCode(fetcher, 308)
                }
                shouldFollowRedirectWith(fetcher, "Html") { htmlRedirect(it) }
                shouldFollowRedirectWith(fetcher, "Js") { jsRedirect(it) }
            }
        }
    }

    private fun shouldFollowRedirectWithStatusCode(
        fetcher: (String) -> Document,
        status: Int,
        expectedResult: String = status.toString()
    ) {
        describe("with status code $status") {
            beforeEach {
                wireMockServer.stubFor(
                    get(urlMatching("/redirect$status"))
                        .willReturn(
                            aResponse()
                                .withStatus(status)
                                .withHeader(LOCATION, "$host/response$status")
                        )
                )
                wireMockServer.stubFor(
                    get(urlMatching("/response$status"))
                        .willReturn(
                            aResponse()
                                .withBody(expectedResult)
                        )
                )
            }
            it("should return the redirected page") {
                val result = fetcher.invoke("$host/redirect$status").body().text()
                Assert.assertEquals(expectedResult, result)
            }
        }
    }

    private fun shouldFollowRedirectWith(
        fetcher: (String) -> Document,
        type: String,
        bodyBuilder: (String) -> String
    ) {
        val expectedResult = "${type}Result"
        describe("when page have $type redirects") {
            beforeEach {
                wireMockServer.stubFor(
                    get(urlMatching("/redirect$type"))
                        .willReturn(
                            aResponse()
                                .withStatus(200)
                                .withBody(bodyBuilder("$host/response$type"))
                        )
                )
                wireMockServer.stubFor(
                    get(urlMatching("/response$type"))
                        .willReturn(
                            aResponse()
                                .withBody(expectedResult)
                        )
                )
            }

            it("should fetch the wanted page") {
                val result = fetcher.invoke("$host/redirect$type").body().text()
                Assert.assertEquals(expectedResult, result)
            }
        }
    }

    private fun jsRedirect(url: String) = "<html>\n" +
        " <head>\n" +
        "  <script>window.googleJavaScriptRedirect=1</script>\n" +
        "  <script>var n={navigateTo:function(b,a,d){if(b!=a&&b.google){if(b.google.r){b.google.r=0;b.location.href=d;a.location.replace(\"about:blank\");}}else{a.location.replace(d);}}};n.navigateTo(window.parent,window,\"$url\");</script>\n" +
        " </head>\n" +
        " <body></body>\n" +
        "</html>"

    private fun htmlRedirect(url: String) = "<html>\n" +
        " <head>\n" +
        "  <noscript>\n<meta http-equiv=\"refresh\" content=\"0;URL='$url'\">\n</noscript>\n" +
        " </head>\n" +
        " <body></body>\n" +
        "</html>"
}
