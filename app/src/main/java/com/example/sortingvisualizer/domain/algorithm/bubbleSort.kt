package com.example.sortingvisualizer.domain.algorithm

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

suspend fun bubbleSort(list: List<Int>, delayInMs: Long = 10): Flow<List<Int>> = flow {
    val newList = list.toMutableList()
    val n = list.size

    for(i in 0 until(n)){
        var swapped = false
        for(j in 1 until(n-i)){
            if(newList[j-1] > newList[j]){
                val temp = newList[j]
                newList[j] = newList[j-1]
                newList[j-1] = temp
                swapped = true
            }
        }
        delay(delayInMs)
        emit(newList)
        if(!swapped){ break }
    }
    emit(newList)
}