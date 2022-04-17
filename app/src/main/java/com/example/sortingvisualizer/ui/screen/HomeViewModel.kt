package com.example.sortingvisualizer.ui.screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sortingvisualizer.domain.algorithm.Algorithm
import com.example.sortingvisualizer.domain.algorithm.bubbleSort
import com.example.sortingvisualizer.domain.algorithm.quickSort
import com.example.sortingvisualizer.domain.algorithm.randomize
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    var uiState by mutableStateOf(HomeUiState())

    init {
        reshuffle()
    }

    fun reshuffle(){
        uiState = uiState.copy(
            numbers = randomize(uiState.numbers)
        )
    }

     fun sort(algorithm: Algorithm){
        when (algorithm) {
            Algorithm.BUBBLE_SORT -> {
                viewModelScope.launch {
                    uiState = uiState.copy(sortRunning = true)

                    bubbleSort(uiState.numbers).collectLatest { newList ->
                        uiState = uiState.copy(numbers = listOf())
                        uiState = uiState.copy(numbers = newList)
                    }

                    uiState = uiState.copy(sortRunning = false)
                }
            }
            Algorithm.QUICK_SORT -> {
                viewModelScope.launch {
                    uiState = uiState.copy(sortRunning = true)

                    quickSort(
                        list = uiState.numbers.toMutableList()
                    ).collectLatest { newList ->
                        uiState = uiState.copy(numbers = listOf())
                        uiState = uiState.copy(numbers = newList)
                    }

                    uiState = uiState.copy(sortRunning = false)
                }
            }
        }
    }
}