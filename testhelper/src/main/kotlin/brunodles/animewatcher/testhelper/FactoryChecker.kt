package brunodles.animewatcher.testhelper

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.animewatcher.explorer.UrlFetcher
import com.greghaskins.spectrum.Spectrum
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.util.Arrays

object FactoryChecker {

    fun checkFactory(pageParser: PageParser, VALID_URLS:Array<String>,
                     INVALID_URLS:Array<String>, currentEpisode: Episode,
                     nextEpisodes: Array<Episode>) {
        describe(pageParser::class.java.simpleName) {

            describe("when check isEpisode") {

                VALID_URLS.forEach { url ->
                    it("should be able to decode the url \"$url\"") {
                        Assert.assertTrue(pageParser.isEpisode(url))
                    }
                }

                INVALID_URLS.forEach { url ->
                    it("should not decode other the url \"$url\"") {
                        Assert.assertFalse(pageParser.isEpisode(url))
                    }
                }
            }

            describe("when episode") {
                val episode = pageParser.episode(VALID_URLS[0])

                describe("when get currentEpisode") {

                    checkEpisode(currentEpisode, episode)
                }

                describe("when get nextEpisodes") {

                    val episodes = episode.nextEpisodes

                    it("should find ${nextEpisodes.size} episodes") {
                        assertEquals(nextEpisodes.size, episodes.size)
                    }

                    for (i in 0 until nextEpisodes.size)
                        episodeShould(episodes, i, nextEpisodes[i])
                }

            }
        }
    }

    private fun episodeShould(episodes: List<Episode>, index: Int, expected: Episode) {
        describe("when get episode at index [$index]") {
            val episode = episodes[index]
            checkEpisode(episode, expected)
        }
    }

    private fun checkEpisode(episode: Episode, expected: Episode) {
        it("should return the correct number") {
            assertEquals(expected.number, episode.number)
        }

        it("should return the correct title") {
            assertEquals(expected.description, episode.description)
        }

        if (expected.image != null)
            it("should return the correct image") {
                assertEquals(expected.image, episode.image)
            }

        if (expected.video != null)
            it("should return the correct video") {
                assertEquals(expected.video, episode.video)
            }

        it("should return the correct animeName") {
            assertEquals(expected.animeName, episode.animeName)
        }

        it("should return the link") {
            assertEquals(expected.link, episode.link)
        }
    }
}