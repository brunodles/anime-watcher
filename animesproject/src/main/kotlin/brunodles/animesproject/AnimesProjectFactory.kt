package brunodles.animesproject

import bruno.animewatcher.explorer.AnimeExplorer
import bruno.animewatcher.explorer.AnimeFactory

object AnimesProjectFactory : AnimeFactory {

    private val EPISODE_URL_REGEX = Regex("animes.zlx.com.br/exibir/\\d+/\\d+")

    override fun isEpisode(url: String): Boolean = url.contains(EPISODE_URL_REGEX)

    override fun episode(url: String): AnimeExplorer =
            AnimesProjectExplorer(url)
}