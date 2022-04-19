package com.example.sortingvisualizer.domain.algorithm

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

suspend fun insertionSort(list: MutableList<Int>): Flow<List<Int>> = flow {
    for (i in (1 until list.size)) {
        val key = list[i]
        var j = i - 1

        while (j >= 0 && key < list[j]) {
            list[j + 1] = list[j]
            j -= 1
        }
        list[j + 1] = key

        delay(10)
        emit(list)
    }

    delay(10)
    emit(list)
}