package brunodles.animesproject

import bruno.animewatcher.explorer.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

class AnimesProjectFinder : AnimeFinder {

    companion object {
        val HOST = "https://animes.zlx.com.br"
        val BACKGROUND_URL_PATTERN = Pattern.compile("background-image:\\surl\\('(.*?)'\\)")
    }

    override fun search(keywork: String): List<Anime> {
        return UrlFetcher("$HOST/listar-series/")
                .data(buildData(busca = keywork))
                .post()
                .select(".serie-block").map {
            val link = HOST + it.href()
            val img = HOST+findImage(it.select(".serie-img").attr("style"))
            val name = it.select(".serie-nome").text()

            val animePage = UrlFetcher(link).get()
            val episodes = animePage.select(".this-serie-videos a").map {
                EpisodeLink(HOST + it.href(), it.text(), img)
            }.toList()

            if (episodes.isEmpty())
                Anime(name, null, link, img, Arrays.asList(EpisodeLink(link, name, img)))
            else
                Anime(name, null, episodes[0].link, img, episodes)
        }
    }

    private fun findImage(style: String): String {
        val matcher = BACKGROUND_URL_PATTERN.matcher(style)
        if (matcher.find())
            return matcher.group(1)
        return ""
    }

    private fun buildData(categoria: Int = 9,
                          letra: String = "[a-z]",
                          qnt: Int = 100,
                          sort: String = "",

                          tipos: List<String> = Arrays.asList("Episódio", "Filme", "Extra"),
                          genero: List<String> = Collections.emptyList(),
                          status: List<String> = Arrays.asList("Em andamento", "Pausado", "Concluído"),
                          criadores: List<String> = Collections.emptyList(),

                          inicioMin: Int = 1950,
                          inicioMax: Int = 2017,

                          notaMin: Float = 0.0F,
                          notaMax: Float = 10.0F,

                          pagina: Int = 1,
                          busca: String = ""): HashMap<String, String> {
        val map = HashMap<String, String>()
        map.put("categoria", categoria.toString())
        map.put("letra", letra)
        map.put("qnt", qnt.toString())
        map.put("sort", sort)
        map.put("tipos", tipos.joinToString("|"))
        map.put("generos", genero.joinToString("|"))
        map.put("status", status.joinToString("|"))
        map.put("criador", criadores.joinToString("|"))
        map.put("inicioMin", inicioMin.toString())
        map.put("inicioMax", inicioMax.toString())
        map.put("notaMin", notaMin.toString())
        map.put("notaMax", notaMax.toString())
        map.put("pagina", pagina.toString())
        map.put("busca", busca)
        return map
    }
}