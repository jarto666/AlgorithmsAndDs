package com.jarto.graphs.models

import com.jarto.graphs.interfaces.Heap

class Heap<T : Comparable<T>> (private val maxSize: Int, val isMax: Boolean = false) : Heap<T> {

    var size = 0
        private set

    private val items = MutableList<T?>(maxSize) { _ -> null }

    override fun readTop(): T? {
        return items[0]
    }

    fun minHeap() {
        for (pos in (size/2)-1 downTo 0) {
            heapify(pos)
        }
    }

    override fun extract(): T {
        var extracted = items[0]
        items[0] = items[--size]
        items.removeAt(size)
        heapify(0)
        return extracted!!
    }

    override fun insert(item: T) {

        if (size >= maxSize) {
            throw Exception("Heap overflow")
        }

        items[size++] = item
        var currentIndex = size-1
        while (items[currentIndex]!!.deeperThan(items[getParentIndex(currentIndex)]!!)) {
            var parentIndex = getParentIndex(currentIndex)
            swap(getParentIndex(currentIndex), currentIndex)
            currentIndex = parentIndex
        }
    }

    private fun heapify(pos: Int) {
        if (!isLeaf(pos)) {
            val leftIndex = getLeftIndex(pos)
            val rightIndex = getRightIndex(pos)
            var largestChildIndex = pos
            val current = items[pos]!!

            if (leftIndex <= size-1 && items[leftIndex]!!.deeperThan(current)) {
                largestChildIndex = leftIndex
            }
            if (rightIndex <= size-1 && items[rightIndex]!!.deeperThan(items[largestChildIndex]!!)) {
                largestChildIndex = rightIndex
            }

            if (largestChildIndex != pos) {
                swap(pos, largestChildIndex);
                heapify(largestChildIndex);
            }
        }
    }

    private fun isLeaf(pos: Int): Boolean {
        return pos >= size / 2 && pos <= size
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

    private fun T.deeperThan(t: T): Boolean {
        return if (!isMax) this < t else this > t
    }
}