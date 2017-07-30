package brunodles.animesproject

import brunodles.animewatcher.explorer.AnimeExplorer
import brunodles.animewatcher.explorer.AnimeFactory

object AnimesProjectFactory : AnimeFactory {

    private val EPISODE_URL_REGEX = Regex("animes.zlx.com.br/exibir/\\d+/\\d+")

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): AnimeExplorer {
        val explorer = AnimesProjectExplorer(url)
        return AnimeExplorer(explorer.currentEpisode(), explorer.nextEpisodes())
    }
}