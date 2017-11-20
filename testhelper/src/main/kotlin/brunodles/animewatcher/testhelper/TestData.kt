package brunodles.animewatcher.testhelper

import brunodles.animewatcher.explorer.Episode

data class TestData(
        val validUrls: Array<String>,
        val invalidUrls: Array<String>,
        val currentEpisode: Episode
)