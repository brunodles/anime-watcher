package brunodles.animacurse

import bruno.animewatcher.explorer.*
import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Pattern

class AnimaCurseFinder : AnimeFinder {

    companion object {
        val INJECTION_PATTERN = Pattern.compile("var\\shtml\\s=\\s\"(.*?)\"")
        val HREF_PATTERN = Pattern.compile("href='(.*?)'")
    }

    override fun search(keywork: String): List<Anime> {
        val html = UrlFetcher.fetchUrl("https://animacurse.moe/?page_id=540").html()
        val injections = mergeInjection(html)
        val searchRegex = Regex(keywork, RegexOption.IGNORE_CASE)
        return Jsoup.parse(injections).select("div.anime").map {
            val img = it.select("img").src()
            val titleRef = it.select("a.title")
            val link = titleRef.attr("href")
            val title = titleRef.text()
            val text = it.select("p.year").text()

            if (title.contains(searchRegex)) {
                val episodes = findEpisodes(link)
                Anime(title, text, episodes[0].link, img, episodes)
            } else {
                Anime(title, text, null, img, Collections.emptyList())
            }
        }.filter { it.title.contains(searchRegex) }.toList()
    }

    private fun findEpisodes(url: String): List<EpisodeLink> {
        return UrlFetcher.fetchUrl(url).select("div.thumbnail").map {
            val link = it.select("a.play").attr("href")
            val image = it.select("img").src()
            val description = it.select(".text .episode").text()

            EpisodeLink(link, description, image)
        }.toList()
    }

    private fun mergeInjection(html: String): String {
        val matcher = INJECTION_PATTERN.matcher(html)
        val list = StringBuilder()
        while (matcher.find())
            list.append(matcher.group(1))
        return list.toString()
    }

    private fun findAlphabetLinks(html: String?): List<String> {
        val matcher = INJECTION_PATTERN.matcher(html)
        val list = ArrayList<String>()
        while (matcher.find())
            list.addAll(findHrefs(matcher.group(1)))
        return list
    }

    private fun findHrefs(text: String): List<String> {
        val matcher = HREF_PATTERN.matcher(text)
        val list = ArrayList<String>()
        while (matcher.find())
            list.add(matcher.group(1))
        return list
    }

}