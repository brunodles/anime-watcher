package brunodles.animewatcher

private val INVALID_TEXT_PATTERN = Regex("[^\\d\\w]+")

fun fixUrlToFirebase(url: String): String = url.replace(INVALID_TEXT_PATTERN, "")