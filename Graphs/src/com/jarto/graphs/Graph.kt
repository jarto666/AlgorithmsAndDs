package com.jarto.graphs

data class Graph(val directed: Boolean = false) {
    val adjacencyMap = HashMap<Int, HashSet<Int>>()

    fun addNode(nodeLeft: Int, nodeRight: Int?) {

        if (nodeRight == null) {
            adjacencyMap.computeIfAbsent(nodeLeft) { HashSet() }
            return
        }

        adjacencyMap
            .computeIfAbsent(nodeLeft) { HashSet() }
            .add(nodeRight)

        if (!directed)
            adjacencyMap
                .computeIfAbsent(nodeRight) { HashSet() }
                .add(nodeLeft)
        else
            adjacencyMap
                .computeIfAbsent(nodeRight) { HashSet() }
    }

    fun getReversed(): Graph {
        if (!directed) return this;

        val graph = Graph(directed)
        this.adjacencyMap.keys.forEach { source ->
            this.adjacencyMap[source]!!.forEach { dest ->
                graph.addNode(dest, source)
            }
        }
        return graph
    }
}

//data class Node(var id: Int, var value: Int) {
//    override fun equals(other: Any?): Boolean {
//        return super.equals(other)
//    }
//
//    override fun hashCode(): Int {
//        return id.hashCode()
//    }
//}