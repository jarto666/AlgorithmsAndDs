package com.jarto.graphs

import java.lang.Exception
import java.util.*
import kotlin.collections.HashSet

fun sccSearch(graph: Graph): MutableMap<Int, HashSet<Int>> {

    class NodeInfo(var visited: Boolean = false, var finishingTime: Int? = null, var leader: Int? = null)

    class VisitMap {
        val map = mutableMapOf<Int, NodeInfo>().apply {
            for (node in graph.adjacencyMap) {
                this[node.key] = NodeInfo()
            }
        }

        fun markExplored(node: Int) {
            if (!map.containsKey(node)) throw Exception()
            map[node]!!.visited = true
        }

        fun isExplored(node: Int): Boolean {
            if (!map.containsKey(node)) throw Exception()
            return map[node]!!.visited
        }

        fun setFinishingTime(node: Int, time: Int) {
            if (!map.containsKey(node)) throw Exception()
            val node = map[node]!!
            node.finishingTime = time
        }

        fun getFinishingTime(node: Int): Int? {
            if (!map.containsKey(node)) throw Exception()
            return map[node]!!.finishingTime
        }

        fun setLeader(node: Int, leader: Int) {
            if (!map.containsKey(node)) throw Exception()
            val node = map[node]!!
            node.leader = leader
        }
    }

    fun dfsLoop(g: Graph): VisitMap {

        var t = 0
        var s = 0
        val visitMap = VisitMap()

        fun dfs(node: Int) {
            visitMap.markExplored(node)
            visitMap.setLeader(node, s)
            for (dest in g.adjacencyMap[node]!!) {
                if (!visitMap.isExplored(dest))
                    dfs(dest)
            }
            visitMap.setFinishingTime(node, ++t)
        }

        fun dfsIterative(node: Int) {
            val stack: Stack<Int> = Stack()
            stack.push(node)
            while (!stack.empty()) {
                var v = stack.pop()!!
                if (!visitMap.isExplored(v)) {
                    visitMap.markExplored(v)
                    for (dest in g.adjacencyMap[node]!!) {
                        stack.push(dest)
                    }
                }
            }
        }

        for (node in g.adjacencyMap.toSortedMap(compareByDescending { it }))
            if (!visitMap.isExplored(node.key)) {
                s = node.key
                dfs(node.key)
            }

        return visitMap
    }

    fun Graph.getFromFinishingTime(map: VisitMap): Graph {
        val graph = Graph(directed)

        this.adjacencyMap.keys.forEach { source ->
            val sourceFinishTime = map.getFinishingTime(source)!!
            this.adjacencyMap[source]!!.forEach { dest ->
                val destFinishTime = map.getFinishingTime(dest)!!
                graph.addNode(sourceFinishTime, destFinishTime)
            }
        }

        return graph
    }

    val finishingTimes = dfsLoop(graph.getReversed())
    val result = dfsLoop(graph.getFromFinishingTime(finishingTimes))

    val leaderMap = mutableMapOf<Int, HashSet<Int>>().apply {
        for (node in result.map){
            this
                .computeIfAbsent(node.value.leader!!) { HashSet() }
                .add(node.key)
        }
    }

    return leaderMap
}