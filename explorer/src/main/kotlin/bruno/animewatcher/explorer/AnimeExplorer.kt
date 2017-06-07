package bruno.animewatcher.explorer

import brunodles.kotlin.annotation.NoArgs
import java.util.*

@NoArgs data class AnimeExplorer(
        val currentEpisode: CurrentEpisode,
        val nextEpisodes: List<EpisodeLink> = Collections.emptyList())