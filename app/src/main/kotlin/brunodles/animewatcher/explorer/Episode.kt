package brunodles.animewatcher.explorer

import java.io.Serializable

data class Episode(
        val description: String,
        val number: Int,
        val animeName: String? = null,
        val image: String? = null,
        val video: String? = null,
        val link: String,
        val nextEpisodes: List<Episode> = arrayListOf(),
        val temporaryVideoUrl: Boolean = false) : Serializable {

    constructor() : this("", 0, link = "")

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
            "nextEpisodes" to nextEpisodes,
            "temporaryVideoUrl" to temporaryVideoUrl
    ).filterValues { it != null }

    fun containsNextEpisodes() = !nextEpisodes.isEmpty()

    fun isPlayable() = video != null
}
