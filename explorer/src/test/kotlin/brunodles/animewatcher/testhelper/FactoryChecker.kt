package brunodles.animewatcher.testhelper

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import com.greghaskins.spectrum.Spectrum.describe
import com.greghaskins.spectrum.Spectrum.it
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import java.lang.AssertionError
import java.util.regex.Pattern

object FactoryChecker {

    fun describe(pageParser: PageParser, block: PageParser.() -> Unit) {
        return describe(pageParser::class.java.simpleName) { block.invoke(pageParser) }
    }

    fun describeFactory(
        pageParser: PageParser, validUrls: Array<String> = emptyArray(),
        invalidUrls: Array<String> = emptyArray(), expectedEpisode: Episode? = null
    ) {
        describe(pageParser) {
            whenCheckIsEpisode(validUrls, invalidUrls)
            expectedEpisode?.let {
                whenEpisode(it)
            }
        }
    }

    fun PageParser.whenCheckIsEpisode(validUrls: Array<String>, invalidUrls: Array<String>) {
        if (validUrls.isNotEmpty() || invalidUrls.isNotEmpty())
            describe("when check isEpisode") {

                validUrls.forEach { url ->
                    it("should be able to decode the url \"$url\"") {
                        assertTrue(this.isEpisode(url))
                    }
                }

                invalidUrls.forEach { url ->
                    it("should not decode other the url \"$url\"") {
                        Assert.assertFalse(this.isEpisode(url))
                    }
                }
            }
    }

    fun PageParser.whenEpisode(expectedEpisode: Episode) {

        describe("when episode") {
            val resultEpisode = this.episode(expectedEpisode.link)

            describe("when get current episode") {
                checkEpisode(expectedEpisode, resultEpisode)
            }

            describe("when get nextEpisodes") {

                val episodes = resultEpisode.nextEpisodes
                val expectedNextEpisodes = expectedEpisode.nextEpisodes

                it("should find ${expectedNextEpisodes.size} episodes") {
                    try {
                        assertEquals(expectedNextEpisodes.size, episodes.size)
                    } catch (e: AssertionError) {
                        if (episodes.isNotEmpty()) {
                            val message = episodes.joinToString(",\n") {
                                "Episode(number = ${it.number}," +
                                    "animeName = \"${it.animeName}\"," +
                                    "image = \"${it.image}\"," +
                                    "description = \"${it.description}\"," +
                                    "link = \"${it.link}\")"
                            }
                            throw AssertionError(e.message + ". found:\n$message", e)
                        } else {
                            throw e
                        }
                    }
                }

                for (index in 0 until expectedNextEpisodes.size)
                    describe("when get episode at index [$index]") {
                        val episode = if (episodes.size > index) episodes[index] else null
                        if (episode == null)
                            fail("should return an episode matching: ${expectedNextEpisodes[index]}")
                        else
                            checkEpisode(expectedNextEpisodes[index], episode)
                    }
            }
        }
    }

    private fun checkEpisode(expected: Episode, episode: Episode) {
        it("should return the correct number") {
            assertEquals(expected.number, episode.number)
        }

        it("should return the correct description") {
            assertEquals(expected.description, episode.description)
        }

        if (expected.image != null || episode.image != null)
            it("should return the correct image") {
                assertEquals(expected.image, episode.image)
            }

        if (expected.video != null || episode.video != null)
            it("should return the correct video") {
                val matches = expected.video?.let { expectedRegex ->
                    if (expectedRegex.isBlank() || episode.video.isNullOrBlank())
                        return@let false
                    Pattern.compile(expectedRegex).matcher(episode.video).matches()
                } ?: false
                val condition = matches || expected.video == episode.video
                assertTrue(
                    "" +
                        "\n Expected \"${expected.video}\"" +
                        "\n      got \"${episode.video}\"", condition
                )
            }

        it("should return the correct temporaryVideoUrl") {
            assertEquals(expected.temporaryVideoUrl, episode.temporaryVideoUrl)
        }

        it("should return the correct animeName") {
            assertEquals(expected.animeName, episode.animeName)
        }

        it("should return the link") {
            assertEquals(expected.link, episode.link)
        }
    }
}