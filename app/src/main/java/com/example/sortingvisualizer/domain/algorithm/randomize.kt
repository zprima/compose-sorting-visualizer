package com.example.sortingvisualizer.domain.algorithm

fun randomize(list: List<Int>): List<Int> {
    val newList = list.toIntArray()
    newList.shuffle()

    return newList.toList()
}