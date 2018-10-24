package brunodles.extensions

import java.util.Random

private fun <E> List<E>.firsts(max: Int): List<E> {
    val lastIndex = if (this.size >= max) max else this.size
    return this.subList(0, lastIndex)
}

private fun <E> List<E>.random(): E = this[Random().nextInt(this.size)]
