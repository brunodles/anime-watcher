package brunodles.anitubex

import bruno.animewatcher.explorer.*
import java.net.URLEncoder
import java.util.*

class AnitubexFinder : AnimeFinder {

    companion object {
        private val UTF8 = "UTF-8"
    }

    override fun search(keywork: String): List<Anime> {
        val doc = UrlFetcher.fetchUrl(searchUrl(keywork))
        return doc.select(".panel-episodios-recientes > table").map {
            val img = it.select(".preview-genero").src()
            val titleRef = it.select(".titulo-genero")
            val title = titleRef.text()
            val href = titleRef.attr("href")

            val episodes = findList(href)
            Anime(title, null, episodes[0].link, img, episodes)
        }.toList()
    }

    private fun findList(url: String): List<EpisodeLink> {
        if (url.isNullOrEmpty())
            return Collections.emptyList()
        return UrlFetcher.fetchUrl(url).select(".lista-episodios a").map {
            val href = it.attr("href")
            val text = it.text()
            EpisodeLink(href, text, null)
        }.toList().reversed()
    }

    private fun searchUrl(keywork: String) = "http://www.anitubex.com/buscar/" + URLEncoder.encode(keywork, UTF8)

}
