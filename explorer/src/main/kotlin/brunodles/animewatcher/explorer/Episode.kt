package brunodles.animewatcher.explorer

import brunodles.kotlin.annotation.NoArgs
import java.io.Serializable

@NoArgs
data class Episode(
        val description: String,
        val image: String? = null,
        val video: String? = null,
        val link: String? = null) : Serializable {

    companion object {
        private const val serialVersionUid: Long = 1L
    }
}