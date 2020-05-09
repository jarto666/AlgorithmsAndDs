package com.jarto.graphs

import com.jarto.graphs.interfaces.Keyed
import com.jarto.graphs.models.GraphWeighted
import com.jarto.graphs.models.HeapImpl
import java.util.HashSet

fun GraphWeighted.dijkstra(startNode: Int): MutableMap<Int, Int> {

    data class HeapNode (override val key: Int, var distance: Int) : Comparable<HeapNode>, Keyed<Int> {
        override fun compareTo(other: HeapNode): Int {
            return distance - other.distance
        }

        override fun equals(other: Any?): Boolean {
            if (other is HeapNode) {
                return other.key == key
            }

            return false
        }

    }

    val heap = HeapImpl<HeapNode, Int>()

    heap.insert(HeapNode(startNode, 0))
    for (entry in this.adjacencyMap.entries) {
        if (entry.key != startNode)
            heap.insert(HeapNode(entry.key, Int.MAX_VALUE))
    }

    val result = mutableMapOf<Int, Int>()

    while (!heap.isEmpty) {
        val node = heap.extract()
        result[node.key] = node.distance
        val adjacentNodes = this.adjacencyMap.entries.first { it.key == node.key }.value
        for (adjacentNode in adjacentNodes) {
            val adjacentHeapNode = heap.getByKey(adjacentNode.first)
            if (adjacentHeapNode != null && adjacentHeapNode.distance > adjacentNode.second + node.distance) {
                val distance = adjacentNode.second + node.distance
                heap.setByKey(adjacentNode.first, adjacentHeapNode.copy(distance = distance))
            }
        }
    }

    return result
}