package bruno.animewatcher.explorer

interface AnimeExplorer {

    fun currentEpisode(): CurrentEpisode

    fun nextEpisodes(): List<NextEpisode>
}
