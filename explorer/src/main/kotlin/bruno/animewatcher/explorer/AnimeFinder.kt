package bruno.animewatcher.explorer

interface AnimeFinder {

    fun search(keywork: String): List<Anime>
}