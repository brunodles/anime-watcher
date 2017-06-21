package bruno.animewatcher.explorer

import brunodles.kotlin.annotation.NoArgs
import java.io.Serializable

@NoArgs
data class Anime(
        val title: String,
        val description: String?,
        val link: String?,
        val imageUrl: String,
        val episodes: List<EpisodeLink>) : Serializable {
    companion object {
        private const val serialVersionUid: Long = 1L
    }
}