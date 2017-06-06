package bruno.animewatcher.explorer

import brunodles.kotlin.annotation.NoArgs

@NoArgs
data class Anime(
        val title: String,
        val description: String?,
        val link: String?,
        val imageUrl: String,
        val episodes: List<EpisodeLink>
)