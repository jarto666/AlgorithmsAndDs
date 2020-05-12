package com.jarto.main

import com.jarto.graphs.breadthFirstSearch
import com.jarto.graphs.dijkstra
import com.jarto.graphs.medianMaintenance
import com.jarto.graphs.models.Graph
import com.jarto.graphs.models.GraphWeighted
import com.jarto.graphs.sccSearch
import java.io.File
import java.util.*
import kotlin.collections.HashSet
import kotlinx.coroutines.*

fun main(args: Array<String>) {
//    val bfsResult = bfs()
//    val sccResult = findAllScc()
//    val dijkstra = findShortestPathsByDijkstra()
//    var s = findMedian()
    var totalSums = findTotalNumbersOfSums()
    println(totalSums)
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

fun findTotalNumbersOfSums(): Int {
    val fileName = "resources/2sum.txt"

    var set = HashSet<Int>()
    var count = 0
    File(fileName).forEachLine {
        val num = it.toInt()
        set.add(num)
//        if (set.contains(num)) return@forEachLine
//
//        set.add(num)
//        for(sum in -10000..10000) {
//            if (num != sum-num && set.contains(sum-num)) {
//                count++
//            }
//        }
    }

    val deferred = (-10000..10000).map { sum ->
        GlobalScope.async {
            for (a in set) {
                if (sum != a*2 && sum-a in set) {
                    return@async 1
                }
            }
            return@async 0
        }
    }
    var sum = 0
    runBlocking {
        sum = deferred.map { it.await() }.sum()
    }

    return sum
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