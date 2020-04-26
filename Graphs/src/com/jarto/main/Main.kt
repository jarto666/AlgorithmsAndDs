package com.jarto.main

import com.jarto.graphs.*
import java.io.File

fun main(args: Array<String>) {
    val fileName = "resources/graph.txt"
    val graph = readGraphFromFile(fileName)

    val depthMap = graph.breadthFirstSearch(1)
}

fun readGraphFromFile(fileName: String): Graph {
    val graph = Graph();

    File(fileName).forEachLine {
        if (it.isNullOrBlank()) return@forEachLine

        val nodes = it.splitIgnoreEmpty('\t').map { it.toInt() }
        val sourceNode = nodes[0]

        if (nodes.size == 1) {
            graph.addNode(sourceNode, null)
        }

        for(i in 1 until nodes.size) {
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