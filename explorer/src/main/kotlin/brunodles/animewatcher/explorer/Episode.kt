package brunodles.animewatcher.explorer

import brunodles.kotlin.annotation.NoArgs
import java.io.Serializable

@NoArgs
data class Episode(
        val description: String,
        val number: Int,
        val animeName: String? = null,
        val image: String? = null,
        val video: String? = null,
        val link: String? = null,
        val nextEpisodes: List<Episode>? = arrayListOf()) : Serializable {

    companion object {
        private const val serialVersionUid: Long = 1L
    }

    fun toMap() = mapOf(
            "description" to description,
            "number" to number,
            "animeName" to animeName,
            "image" to image,
            "video" to video,
            "link" to link,
            "nextEpisodes" to nextEpisodes//?.map { it.link }?.toList()
    ).filterValues { it != null }
}