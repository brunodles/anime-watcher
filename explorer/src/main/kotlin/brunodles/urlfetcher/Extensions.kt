package brunodles.urlfetcher

import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URLEncoder

private val UTF8 = "UTF-8"
fun Elements.src() = this.attr("src").trim()
fun Element.src() = this.attr("src").trim()
fun Element.alt() = this.attr("alt")
fun Elements.alt() = this.attr("alt")
fun Elements.href() = this.attr("href")
fun Element.href() = this.attr("href")
fun String.max(max: Int) = this.substring(0, if (length < max) length else max)
fun String.encodeUTF8() = URLEncoder.encode(this, UTF8)
