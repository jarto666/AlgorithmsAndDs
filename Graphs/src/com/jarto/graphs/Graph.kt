package com.jarto.graphs

class Graph() {
    val adjacencyMap = HashMap<Int, HashSet<Int>>()

    fun addNode(nodeLeft: Int, nodeRight: Int?) {

        if (nodeRight == null) {
            adjacencyMap.computeIfAbsent(nodeLeft) { HashSet() }
            return
        }

        adjacencyMap
            .computeIfAbsent(nodeLeft) { HashSet() }
            .add(nodeRight)

        adjacencyMap
            .computeIfAbsent(nodeRight) { HashSet() }
            .add(nodeLeft)
    }
}