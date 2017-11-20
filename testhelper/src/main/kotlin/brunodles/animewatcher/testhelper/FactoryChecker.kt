package brunodles.animewatcher.testhelper

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.Assert.assertEquals

object FactoryChecker {

    fun checkFactory(pageParser: PageParser, testData: TestData) =
            checkFactory(pageParser, testData.validUrls, testData.invalidUrls,
                    testData.currentEpisode)

    fun checkFactory(pageParser: PageParser, validUrls: Array<String>,
                     invalidUrls: Array<String>, currentEpisode: Episode) {
        describe(pageParser::class.java.simpleName) {

            describe("when check isEpisode") {

                validUrls.forEach { url ->
                    it("should be able to decode the url \"$url\"") {
                        Assert.assertTrue(pageParser.isEpisode(url))
                    }
                }

                invalidUrls.forEach { url ->
                    it("should not decode other the url \"$url\"") {
                        Assert.assertFalse(pageParser.isEpisode(url))
                    }
                }
            }

            describe("when episode") {
                val episode = pageParser.episode(validUrls[0])

                describe("when get currentEpisode") {

                    checkEpisode(currentEpisode, episode)
                }

                describe("when get nextEpisodes") {

                    val episodes = episode.nextEpisodes
                    val expectedNextEpisodes = currentEpisode.nextEpisodes

                    it("should find ${expectedNextEpisodes.size} episodes") {
                        assertEquals(expectedNextEpisodes.size, episodes.size)
                    }

                    for (i in 0 until expectedNextEpisodes.size)
                        episodeShould(episodes, i, expectedNextEpisodes[i])
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