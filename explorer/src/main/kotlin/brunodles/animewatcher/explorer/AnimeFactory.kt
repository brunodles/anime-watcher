package brunodles.animewatcher.explorer

interface AnimeFactory {

    fun isEpisode(url: String): Boolean

    fun episode(url: String): AnimeExplorer
}

val FACTORIES = ArrayList<AnimeFactory>()