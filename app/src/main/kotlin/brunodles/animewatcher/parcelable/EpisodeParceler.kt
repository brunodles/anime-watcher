package brunodles.animewatcher.parcelable

import brunodles.animewatcher.explorer.Episode

object EpisodeParceler {

    fun toParcel(episode: Episode) = EpisodeParcel(episode)

    fun fromParcel(episode: EpisodeParcel): Episode = Episode(
            episode.description,
            episode.number,
            episode.animeName,
            episode.image,
            episode.video,
            episode.link,
            episode.nextEpisodes.map { fromParcel(it) }.toList()
    )
}