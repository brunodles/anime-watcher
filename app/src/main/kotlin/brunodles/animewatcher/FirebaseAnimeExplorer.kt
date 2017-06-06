package brunodles.animewatcher

import bruno.animewatcher.explorer.AnimeExplorer
import bruno.animewatcher.explorer.CurrentEpisode
import bruno.animewatcher.explorer.EpisodeLink
import java.util.*

data class FirebaseAnimeExplorer(
        var currentEpisode: CurrentEpisode? = null,
        var nextEpisodes: List<EpisodeLink> = Collections.emptyList()) : AnimeExplorer {

    override fun currentEpisode(): CurrentEpisode = currentEpisode!!

    override fun nextEpisodes(): List<EpisodeLink> = nextEpisodes
}