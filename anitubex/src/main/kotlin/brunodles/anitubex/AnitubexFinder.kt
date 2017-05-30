package brunodles.anitubex

import bruno.animewatcher.explorer.Anime
import bruno.animewatcher.explorer.AnimeFinder
import java.util.*

class AnitubexFinder : AnimeFinder{
    override fun search(keywork: String): List<Anime> {
        // http://www.anitubex.com/buscar/one+piece/
        return Collections.emptyList()
    }

}
