package com.example.sortingvisualizer.domain.algorithm

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//suspend fun bubbleSort(list: List<Int>, onUpdateList: (MutableList<Int>) -> Unit) {
//        val newList = list.toMutableList()
//        val n = list.size
//
//        for(i in 0 until(n)){
//            for(j in 1 until(n-i)){
//                if(newList[j-1] > newList[j]){
//                    val temp = newList[j]
//                    newList[j] = newList[j-1]
//                    newList[j-1] = temp
//
//                    Log.d("APP", "Inside BubbleSort ${newList.toString()}")
//                    delay(100)
//                    onUpdateList(newList)
//                }
//            }
//        }
//
//        Log.d("APP", "BubbleSort done: ${newList.toString()}")
//        onUpdateList(newList)
//}

suspend fun bubbleSort(list: List<Int>, delayInMs: Long = 50): Flow<List<Int>> = flow {
    val newList = list.toMutableList()
    val n = list.size

    for(i in 0 until(n)){
        for(j in 1 until(n-i)){
            if(newList[j-1] > newList[j]){
                val temp = newList[j]
                newList[j] = newList[j-1]
                newList[j-1] = temp

                delay(delayInMs)
                emit(newList)
            }
        }
    }
    emit(newList)
}