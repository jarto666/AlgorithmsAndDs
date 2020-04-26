package com.jarto.graphs

import java.util.*

fun Graph.breadthFirstSearch(startNode: Int): MutableMap<Int, Int> {

    val graph = this

    val depthMap = mutableMapOf<Int, Int>().apply {
        for (node in graph.adjacencyMap) {
            this[node.key] = 0
        }
    }

    class VisitedMap {
        val visitedList = mutableMapOf<Int, Boolean>().apply {
            for (node in graph.adjacencyMap) {
                this[node.key] = false
            }
        }

        fun setVisited(key: Int) {
            visitedList[key] = true
        }

        fun isExplored(key: Int): Boolean {
            return visitedList[key]!!;
        }
    }

    val queue = LinkedList<Int>()

    val visitedMap = VisitedMap()
    visitedMap.setVisited(startNode)
    queue.add(startNode)

    var depth = 0;
    depthMap[startNode] = depth;

    while (!queue.isEmpty()) {
        val currentNode = queue.remove()
        for (node in graph.adjacencyMap[currentNode]!!) {
            if (!visitedMap.isExplored(node)) {
                visitedMap.setVisited(node)
                queue.add(node)
                depthMap[node] = depthMap[currentNode]!!+1;
            }
        }
    }
    return depthMap
}