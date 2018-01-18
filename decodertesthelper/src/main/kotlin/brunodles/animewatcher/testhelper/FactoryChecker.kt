package brunodles.animewatcher.testhelper

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.util.regex.Pattern

object FactoryChecker {

    fun checkFactory(pageParser: PageParser, validUrls: Array<String> = emptyArray(),
                     invalidUrls: Array<String> = emptyArray(), expectedEpisode: Episode?=null) {
        describe(pageParser::class.java.simpleName) {

            if (validUrls.isNotEmpty() || invalidUrls.isNotEmpty())
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

            expectedEpisode?.let {
                whenEpisode(pageParser, expectedEpisode)
            }
        }
    }

    private fun whenEpisode(pageParser: PageParser, expectedEpisode: Episode) {

        describe("when episode") {
            val resultEpisode = pageParser.episode(expectedEpisode.link!!)

            describe("when get currentEpisode") {
                checkEpisode(expectedEpisode, resultEpisode)
            }

            describe("when get nextEpisodes") {

                val episodes = resultEpisode.nextEpisodes!!
                val expectedNextEpisodes = expectedEpisode.nextEpisodes!!

                it("should find ${expectedNextEpisodes.size} episodes") {
                    assertEquals(expectedNextEpisodes.size, episodes.size)
                }

                for (index in 0 until expectedNextEpisodes.size)
                    describe("when get episode at index [$index]") {
                        val episode = if (episodes.size > index) episodes[index] else null
                        checkEpisode(expectedNextEpisodes[index], episode)
                    }
            }

        }
    }

    private fun checkEpisode(expected: Episode, episode: Episode?) {
        it("should return the correct number") {
            assertEquals(expected.number, episode?.number)
        }

        it("should return the correct description") {
            assertEquals(expected.description, episode?.description)
        }

        if (expected.image != null)
            it("should return the correct image") {
                assertEquals(expected.image, episode?.image)
            }

        if (expected.video != null)
            it("should return the correct video") {
                val matches = Pattern.compile(expected.video).matcher(episode?.video).matches()
                val condition = matches || expected.video == episode?.video
                assertTrue("" +
                        "\n Expected \"${expected.video}\"" +
                        "\n      got \"${episode?.video}\"", condition)
            }

        it("should return the correct temporaryVideoUrl") {
            assertEquals(expected.temporaryVideoUrl, episode?.temporaryVideoUrl)
        }

        it("should return the correct animeName") {
            assertEquals(expected.animeName, episode?.animeName)
        }

        it("should return the link") {
            assertEquals(expected.link, episode?.link)
        }
    }
}