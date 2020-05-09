package com.jarto.graphs.interfaces

interface Heap<T : Comparable<T>> {
    fun insert(key: T)
    fun extract(): T
    fun readTop(): T?
}