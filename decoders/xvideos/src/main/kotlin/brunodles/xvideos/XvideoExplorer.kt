package brunodles.xvideos

import brunodles.animewatcher.explorer.Episode
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class XvideoExplorer(val doc: Document) {

    private val mainDoc: Elements = doc.body().select("#main")

    fun currentVideo(): Episode {

        val title = findTitle(mainDoc)
        val videoLink = findVideoLink(mainDoc)
        val nextVideos = nextVideos()
        return Episode(
                animeName = title,
                video = videoLink,
                number = 1,
                link = videoLink,
                description = title,
                image = "",
                nextEpisodes = nextVideos
        )
    }

    fun nextVideos(): List<Episode> {
        val relatedVideoScript = extractRelatedVideoScript(mainDoc)
        val json = extractRelatedVideoJson(relatedVideoScript)
        val type = object : TypeToken<List<RelatedVideoDTO>>() {}.type
        val relatedVideoList = Gson().fromJson<List<RelatedVideoDTO>>(json, type)
        var index = 1
        return relatedVideoList
                .map {
                    Episode(description = it.duration,
                            number = ++index,
                            animeName = it.fullTitle,
                            image = it.image,
                            video = it.videoLink.formatUrl(),
                            link = it.videoLink.formatUrl())
                }
    }

    private fun String.formatUrl() = "http://xvideos.com$this"

    private fun findTitle(doc: Elements): String {
        val titleElement = doc.select("h2")
        titleElement.select("span").remove()
        return titleElement.text()
    }

    private fun  findVideoLink(doc: Elements): String {
        val html5PlayerList = extractHtml5Script(doc)
                .toString()
                .removePrefix("<script>")
                .removeSuffix("</script>")
                .replace("\n", "")
                .replace("\t", "")
                .split(";")

        val videoUrl = html5PlayerList.find { it.contains("setVideoUrlHigh") }

        return extractJSSetterValue(videoUrl)
    }

    private fun extractJSSetterValue(setter: String?): String {
        if (setter != null) {
            val start = setter.indexOf("(")
            val end = setter.lastIndexOf(")")
            return setter.substring(start + 1, end).removeSurrounding("'")
        } else {
            return ""
        }
    }

    private fun extractRelatedVideoJson(element: Element?): String {
        val plainScript = element.toString()
                .removePrefix("<script>")
                .removeSuffix("</script>")
        val start = plainScript.indexOf("[")
        val end = plainScript.lastIndexOf("]")

        return plainScript
                .substring(start, end+1)
    }

    private fun extractHtml5Script(doc: Elements): Element? {
        return  doc.select("script")
                .filter { it.attributes().none() }
                .find { it.toString().contains("setVideoUrlHigh") }
    }

    private fun extractRelatedVideoScript(doc: Elements): Element? {
        return doc.select("script")
                .filter { it.attributes().none() }
                .find { it.toString().contains("video_related") }

    }

}