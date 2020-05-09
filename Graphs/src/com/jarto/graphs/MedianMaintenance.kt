package com.jarto.graphs

import com.jarto.graphs.models.Heap

fun medianMaintenance(nums: IntArray): Int {

    val minHeap = Heap<Int>(nums.size)
    val maxHeap = Heap<Int>(nums.size, isMax = true)

    var medianSum = 0

    fun getMedian(): Int {
        val min = minHeap.readTop()!!
        val max = maxHeap.readTop()!!

        return when {
            minHeap.size > maxHeap.size -> min
            else -> max
        }
    }

    nums.forEach {
        val num = it
        if (minHeap.size == 0) {
            minHeap.insert(num)
            minHeap.minHeap()
            medianSum += num
        } else if (maxHeap.size == 0) {
            maxHeap.insert(num)
            maxHeap.minHeap()
            medianSum += num
        } else {
            var currentMedian = getMedian()

            if (num > currentMedian) {
                minHeap.insert(num)
                minHeap.minHeap()
            } else {
                maxHeap.insert(num)
                maxHeap.minHeap()
            }

            val minMaxDiff = minHeap.size - maxHeap.size
            if (minMaxDiff > 1) {
                val extracted = minHeap.extract()
                maxHeap.insert(extracted)
                maxHeap.minHeap()
            } else if (minMaxDiff < -1) {
                val extracted = maxHeap.extract()
                minHeap.insert(extracted)
                minHeap.minHeap()
            }

            medianSum += getMedian()
        }
    }

    return medianSum % 10000
}