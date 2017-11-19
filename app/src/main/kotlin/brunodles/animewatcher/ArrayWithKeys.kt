package brunodles.animewatcher

import android.support.v4.util.ArrayMap

class ArrayWithKeys<KEY, ITEM> : MutableCollection<ITEM> {

    private val list: ArrayList<ITEM> = ArrayList()
    private val keyMap = ArrayMap<KEY, Int>()

    override var size: Int = 0
        get() = list.size

    override fun isEmpty() = list.isEmpty()

    override fun clear() {
        list.clear()
        keyMap.clear()
    }

    override fun addAll(elements: Collection<ITEM>) = list.addAll(elements)

    override fun add(element: ITEM) = add(element, null) >= 0

    /**
     * @return index of the added item, -1 if invalid
     */
    fun add(element: ITEM, key: KEY? = null): Int {
        if (keyMap.contains(key))
            return -1
        synchronized(this) {
            val index = list.size
            list.add(index, element)
            keyMap.put(key, index)
            return index
        }
    }

    fun replace(element: ITEM, key: KEY): Int {
        if (!keyMap.contains(key))
            return -1
        val index = keyMap[key]!!
        list.removeAt(index)
        list.add(index, element)
        return index
    }

    override fun remove(element: ITEM): Boolean {
        val index = list.indexOf(element)
        if (index < 0) return false
        list.removeAt(index)
        val removeKeys = ArrayList<KEY>()
        for (key in keyMap)
            if (index == key.value)
                removeKeys.add(key.key)
        for (key in removeKeys)
            keyMap.remove(key)
        return true
    }

    /**
     * @return the Index of the removed element
     */
    fun removeByKey(key: KEY): Int {
        val index = keyMap[key]
        if (index == null || index < 0)
            return -1
        list.removeAt(index)
        keyMap.remove(key)
        return index
    }

    override fun removeAll(elements: Collection<ITEM>): Boolean {
        for (element in elements)
            if (!remove(element))
                return false
        return true
    }

    override fun retainAll(elements: Collection<ITEM>): Boolean = list.retainAll(elements)

    override fun contains(element: ITEM): Boolean = list.contains(element)

    override fun containsAll(elements: Collection<ITEM>): Boolean = list.containsAll(elements)

    override fun iterator(): MutableIterator<ITEM> = list.iterator()

    operator fun get(position: Int): ITEM = list[position]

    operator fun get(key: KEY): ITEM? = keyMap[key]?.let { list[it] }

}