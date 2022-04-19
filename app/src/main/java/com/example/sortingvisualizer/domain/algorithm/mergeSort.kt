package com.example.sortingvisualizer.domain.algorithm

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

suspend fun mergeSort(list: List<Int>, delayInMs:Long = 10): Flow<List<Int>> = flow {
    val newList = list.toMutableList()

    if(newList.size <= 1) {
        emit(newList)
        return@flow
    }

    val mid = newList.size / 2
    val left = newList.subList(0, mid).toList()
    val right = newList.subList(mid, newList.size).toList()

    var leftSorted = listOf<Int>()
    mergeSort(left).collect {
        leftSorted = it;
        emit(leftSorted + right)
    }

    var rightSorted = listOf<Int>()
    mergeSort(right).collect {
        rightSorted = it;
        emit(leftSorted + rightSorted)
    }

    var i = 0
    var j = 0
    var k = 0

    while (i<leftSorted.size && j < rightSorted.size){
        if(leftSorted[i] < rightSorted[j]){
            newList[k] = leftSorted[i]
            i += 1
        } else {
            newList[k] = rightSorted[j]
            j += 1
        }
        k += 1
    }

    while(i < leftSorted.size){
        newList[k] = leftSorted[i]
        i += 1
        k += 1
    }

    while(j < rightSorted.size){
        newList[k] = rightSorted[j]
        j += 1
        k += 1
    }

    delay(delayInMs)
    emit(newList)
}