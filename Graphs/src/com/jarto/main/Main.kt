package com.jarto.main

import com.jarto.graphs.*
import com.jarto.graphs.models.*
import java.io.File

fun main(args: Array<String>) {
//    val bfsResult = bfs()
//    val sccResult = findAllScc()
//    val dijkstra = findShortestPathsByDijkstra()
    var s = findMedian()
}

/**
 * Executes Breadth-first search on a graph.
 *
 * Returns depth map
 */
fun bfs(): MutableMap<Int, Int> {
    val fileName = "resources/graph.txt"
    val graph = readGraphFromFile(fileName, false)

    return graph.breadthFirstSearch(1)
}

/**
 * Executes Depth-first approach on a graph to find all Strongly-Connected Components.
 *
 * Returns SCCs grouped by leader
 */
fun findAllScc(): Map<Int, HashSet<Int>> {
    val fileName = "resources/directed_huge.txt"
    val graph = readGraphFromFile(fileName, true)
    val result = sccSearch(graph)
    return result.toList().sortedByDescending { (_, value) -> value.size }.toMap()
}

/**
 * Executes Dijkstra algorithm to find all shortest paths in graph starting with node 1.
 *
 * Returns map of Nodes and their matching shortest path lengths
 */
fun findShortestPathsByDijkstra(): MutableMap<Int, Int> {
    val fileName = "resources/weighted_graph.txt"
    val graph = readWeightedGraphFromFile(fileName)
    return graph.dijkstra(1)
}

fun findMedian(): Int {
    val fileName = "resources/median.txt"

    val nums = IntArray(10000)
    var i = 0
    File(fileName).forEachLine {
        nums[i++] = it.toInt()
    }

    return medianMaintenance(nums)
}

fun readGraphFromFile(fileName: String, directed: Boolean): Graph {
    val graph = Graph(directed);

    File(fileName).forEachLine {
        //if (it.isNullOrBlank()) return@forEachLine

        val nodes = it.splitIgnoreEmpty('\t', ' ').map { it.toInt() }
        val sourceNode = nodes[0]

        if (nodes.size == 1) {
            graph.addNode(sourceNode, null)
        }

        for (i in 1 until nodes.size) {
            if (sourceNode != nodes[i])
                graph.addNode(sourceNode, nodes[i])
        }
    }

    return graph
}

fun readWeightedGraphFromFile(fileName: String): GraphWeighted {
    val graph = GraphWeighted();

    File(fileName).forEachLine {
        //if (it.isNullOrBlank()) return@forEachLine

        val nodes = it.splitIgnoreEmpty('\t', ' ')
        val sourceNode = nodes[0].toInt()

        for (i in 1 until nodes.size) {
            val pair = nodes[i].splitIgnoreEmpty(',').map { it.toInt() }
            graph.addNode(sourceNode, pair[0], pair[1])
        }
    }
    
    return graph
}

fun CharSequence.splitIgnoreEmpty(vararg delimiters: Char): List<String> {
    return this.split(*delimiters).filter {
        it.isNotEmpty()
    }
}