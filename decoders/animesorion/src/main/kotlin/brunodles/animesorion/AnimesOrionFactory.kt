package brunodles.animacurse

import brunodles.animewatcher.explorer.*

object AnimesOrionFactory : PageParser {

    private val URL_REGEX = Regex("www.animesorion.tv/\\d+")

    override fun isEpisode(url: String): Boolean =
            url.contains(URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.fetchUrl(url)
        val texts = doc.title().split("Episódio").map { it.trim() }
        val number = texts[1].toIntOrNull() ?: 0
        val animeName = texts[0].trim()
        return Episode(number = number,
                description = "Episódio $number",
                animeName = animeName,
                link = url,
                video = doc.select("video source").src(),
                nextEpisodes = arrayListOf(
                        Episode(number = number + 1,
                                animeName = animeName,
                                description = "Episódio ${number + 1}",
                                link = doc.select(".botoes_anterior_proximos a").last().href()
                        )
                )
        )
    }

}

