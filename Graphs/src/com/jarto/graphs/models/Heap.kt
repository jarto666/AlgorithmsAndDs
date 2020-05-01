package com.jarto.graphs.models

interface Keyed<T> {
    val key: T
}

class MinHeap<T : Comparable<T>, V> () where T : Keyed<V> {
    private val items = mutableListOf<T>()

    private val size: Int
        get() = items.size

    val isEmpty: Boolean
        get() = items.isEmpty()

    fun isInHeap(key: T): Boolean {
        return items.any() {it == key}
    }

    fun getByKey(key: V): T? {
        return items.firstOrNull { it.key == key }
    }

    fun setByKey(key: V, newValue: T) {
        val index = items.indexOfFirst { it.key == key }
        val oldValue = items[index]
        items[index] = newValue

        if (newValue < oldValue) {
            decreaseKey(index)
        }
    }

    fun decreaseKey(sourceIndex: Int) {
        var currentIndex = sourceIndex

        do {
            if (currentIndex == 0) return

            var parentIndex = getParentIndex(currentIndex)

            if (parentIndex >= 0 && (items[parentIndex] < items[currentIndex]))
                return

            swap(parentIndex, currentIndex)
            currentIndex = parentIndex
        } while (true)
    }

    fun insert(key: T) {
        items.add(key)

        if (size == 1) return

        var currentIndex = size - 1

        do {
            var parentIndex = getParentIndex(currentIndex)

            if (parentIndex >= 0 && (items[parentIndex] < items[currentIndex]))
                return

            swap(parentIndex, currentIndex)
            currentIndex = parentIndex
        } while (true)
    }

    fun extract(): T {
        val extracted = items[0]

        items[0] = items[size-1]
        items.removeAt(size-1)

        if (isEmpty) return extracted

        var currentIndex = 0

        loop@ do {
            val leftIndex = getLeftIndex(currentIndex)
            val rightIndex = getRightIndex(currentIndex)

            val left = items.elementAtOrNull(leftIndex)
            val right = items.elementAtOrNull(rightIndex)
            val current = items[currentIndex]
            when {
                left == null && right == null -> break@loop
                left == null -> if (right!! < current)  {
                    swap(currentIndex, rightIndex)
                    currentIndex = rightIndex
                } else break@loop
                right == null -> if (left!! < current) {
                    swap(currentIndex, leftIndex)
                    currentIndex = leftIndex
                } else break@loop
                else -> {
                    currentIndex = if (left < right && left < current) {
                        swap(currentIndex, leftIndex)
                        leftIndex
                    } else if (right < left && right < current) {
                        swap(currentIndex, rightIndex)
                        rightIndex
                    } else break@loop
                }
            }

        } while (true)

        return extracted
    }

    private fun getParentIndex(i: Int): Int {
        return (i - 1) / 2
    }

    // to get index of left child of node at index i
    private fun getLeftIndex(i: Int): Int {
        return 2 * i + 1
    }

    // to get index of right child of node at index i
    private fun getRightIndex(i: Int): Int {
        return 2 * i + 2
    }

    private fun swap(index1: Int, index2: Int) {
        val tmp = items[index1]
        items[index1] = items[index2]
        items[index2] = tmp
    }
}