package brunodles.animewatcher.explorer

import brunodles.kotlin.annotation.NoArgs
import java.io.Serializable

@NoArgs
data class EpisodeLink(
        val link: String,
        val description: String,
        val image: String? = null) : Serializable {

    companion object {
        private const val serialVersionUid: Long = 1L
    }
}
