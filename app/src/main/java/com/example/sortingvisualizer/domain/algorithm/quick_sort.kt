package com.example.sortingvisualizer.domain.algorithm

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

suspend fun quickSort(list: List<Int>, delayInMs: Long = 50): Flow<List<Int>> = flow{
    if(list.size < 2){
        emit(list)
        return@flow
    }

    val pivot = list[list.size / 2]
    val middle = list.filter { it == pivot }
    val less = list.filter { it < pivot }
    val grater = list.filter { it > pivot }

    emit(less + middle + grater)

    var sortedLess = listOf<Int>()
    quickSort(less).collect {
        sortedLess = it
        emit((it + middle + grater))
    }

    var sortedGrater = listOf<Int>()
    quickSort(grater).collect {
        sortedGrater = it
        emit(sortedLess + middle + it)
    }

    delay(delayInMs)
    emit(sortedLess + middle + sortedGrater)
}
