package bruno.animewatcher.explorer

data class Anime(
        val title: String,
        val description: String,
        val link: String,
        val imageUrl: String,
        val episodes: List<EpisodeLink>
)