package brunodles.urlfetcher

internal object Logger {

    var useLog: Boolean = false

    internal fun log(function: () -> String) {
        if (useLog)
            println("UrlFetcher: ${function()}")
    }
}