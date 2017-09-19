package brunodles.animewatcher.explorer

import brunodles.kotlin.annotation.NoArgs
import java.io.Serializable
import java.util.*

@NoArgs data class AnimeExplorer(
        val currentEpisode: Episode,
        val nextEpisodes: List<Episode> = Collections.emptyList()) : Serializable {

    companion object {
        const val serialVersionUid: Long = 1L
    }
}