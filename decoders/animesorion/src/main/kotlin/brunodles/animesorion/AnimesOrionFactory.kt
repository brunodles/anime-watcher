package brunodles.animesorion

import brunodles.animewatcher.explorer.Episode
import brunodles.animewatcher.explorer.PageParser
import brunodles.urlfetcher.UrlFetcher
import brunodles.urlfetcher.href
import brunodles.urlfetcher.src
import org.jsoup.nodes.Document

object AnimesOrionFactory : PageParser {

    private val URL_REGEX = Regex("^(:?https?://)?(:?www.)?animesorion.tv/\\d+.*?$")
    private val NUMBER_REGEX = Regex("\\d+")

    override fun isEpisode(url: String): Boolean =
            url.matches(URL_REGEX)

    override fun episode(url: String): Episode {
        val doc = UrlFetcher.fetchUrl(url)
        return if (doc.title().contains("todos", true)) {
            aboutPage(url, doc)
        } else {
            playerPage(url, doc)
        }
    }

    private fun aboutPage(url: String, doc: Document): Episode {
        val nextEpisodes = doc.select("#listadeepisodios li a")
                .map {
                    val animeName = doc.title().split("todos", ignoreCase = true)[0].trim()
                    val pair: Pair<Int, String> = if (it.text().contains("Episódio", true)) {
                        val number = titleInfo(it.text()).first
                        Pair(number, "Episódio $number")
                    } else {
                        Pair(0, it.text())
                    }
                    Episode(number = pair.first,
                            description = pair.second,
                            animeName = animeName,
                            link = it.href())
                }
                .sortedBy { it.number }
                .toMutableList()
        val firstEpisodeUrl = nextEpisodes.removeAt(0).link!!
        val firstEpisodeDoc = UrlFetcher.fetchUrl(firstEpisodeUrl)
        return extractEpisodeInfoFromPlayerPage(firstEpisodeUrl, firstEpisodeDoc)
                .copy(link = url, nextEpisodes = nextEpisodes)
    }

    private fun playerPage(url: String, doc: Document): Episode {
        val episodeInfo = extractEpisodeInfoFromPlayerPage(url, doc)

        val nextEpisodesUrl = doc.select("div.single #botaos_center_lista a")?.href()
        var nextEpisodes: List<Episode> = emptyList()
        if (nextEpisodesUrl != null && nextEpisodesUrl.isNotBlank())
            try {
                //                val nextEpisodes = aboutAnimeEpisodeList(animeName)
            } catch (e: Exception) {
            }
        if (nextEpisodes.isEmpty())
            nextEpisodes = singNextEpisode(episodeInfo.number, episodeInfo.animeName!!, doc)

        return episodeInfo.copy(nextEpisodes = nextEpisodes)
    }

    private fun extractEpisodeInfoFromPlayerPage(url: String, doc: Document): Episode {
        val titleInfo = titleInfo(doc.title())
        return Episode(number = titleInfo.first,
                description = "Episódio ${titleInfo.first}",
                animeName = titleInfo.second,
                link = url,
                video = doc.select("video source").src()
        )
    }

    private fun singNextEpisode(number: Int, animeName: String, doc: Document): ArrayList<Episode>
            = arrayListOf(Episode(number = number + 1,
            animeName = animeName,
            description = "Episódio ${number + 1}",
            link = doc.select(".botoes_anterior_proximos a").last().href()
    ))

    private fun titleInfo(text: String): Pair<Int, String> {
        val texts = text.split("Episódio", "/", ignoreCase = true).map { it.trim() }
        val number = if (texts.size >= 2) texts[1].toIntOrNull() ?: 999 else 999
        return Pair(number, texts[0])
    }
}

