package com.jarto.main

import com.jarto.graphs.*
import java.io.File

fun main(args: Array<String>) {
    val bfsResult = bfs()
    val sccResult = findAllScc()
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

fun readGraphFromFile(fileName: String, directed: Boolean): Graph {
    val graph = Graph(directed);

    File(fileName).forEachLine {
        //if (it.isNullOrBlank()) return@forEachLine

        val nodes = it.splitIgnoreEmpty('\t', ' ').map { it.toInt() }
        val sourceNode = nodes[0]

        if (nodes.size == 1) {
            graph.addNode(sourceNode, null)
        }

        for(i in 1 until nodes.size) {
            if (sourceNode != nodes[i])
                graph.addNode(sourceNode, nodes[i])
        }
    }

    return graph
}

fun CharSequence.splitIgnoreEmpty(vararg delimiters: Char): List<String> {
    return this.split(*delimiters).filter {
        it.isNotEmpty()
    }
}