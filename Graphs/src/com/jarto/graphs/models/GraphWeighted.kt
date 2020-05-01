package com.jarto.graphs.models

class GraphWeighted {
    val adjacencyMap = HashMap<Int, HashSet<Pair<Int, Int>>>()

    fun addNode(nodeLeft: Int, nodeRight: Int, weight: Int) {

        adjacencyMap
            .computeIfAbsent(nodeLeft) { HashSet() }
            .add(Pair(nodeRight, weight))

        adjacencyMap
            .computeIfAbsent(nodeRight) { HashSet() }
            .add(Pair(nodeLeft, weight))
    }
}