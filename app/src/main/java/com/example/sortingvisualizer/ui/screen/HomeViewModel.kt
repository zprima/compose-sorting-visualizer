package com.example.sortingvisualizer.ui.screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sortingvisualizer.domain.algorithm.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
    private var sortingJob: Job? = null

    init {
        reshuffle()
    }

    fun reshuffle() {
        if(sortingJob?.isActive == true){
            return
        }
        uiState = uiState.copy(
            numbers = randomize(uiState.numbers)
        )
    }

    fun stopSort() {
        sortingJob?.cancel()
        uiState = uiState.copy(sortRunning = false)
    }

    fun sort(algorithm: Algorithm) {
        if(uiState.sortRunning){
            sortingJob?.cancel()
        }

        uiState = uiState.copy(selectedSortAlgorithm = algorithm)

        when (algorithm) {
            Algorithm.BUBBLE_SORT -> {
                sortingJob = viewModelScope.launch {
                    uiState = uiState.copy(sortRunning = true)

                     bubbleSort(uiState.numbers).collectLatest { newList ->
                        uiState = uiState.copy(numbers = listOf())
                        uiState = uiState.copy(numbers = newList)
                    }

                    uiState = uiState.copy(sortRunning = false)
                }
            }
            Algorithm.QUICK_SORT -> {
                sortingJob = viewModelScope.launch {
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
            Algorithm.MERGE_SORT -> {
                sortingJob = viewModelScope.launch {
                    uiState = uiState.copy(sortRunning = true)

                    mergeSort(
                        list = uiState.numbers.toMutableList()
                    ).collectLatest { newList ->
                        uiState = uiState.copy(numbers = listOf())
                        uiState = uiState.copy(numbers = newList)
                    }

                    uiState = uiState.copy(sortRunning = false)
                }
            }
            Algorithm.INSERTION_SORT -> {
                sortingJob = viewModelScope.launch {
                    uiState = uiState.copy(sortRunning = true)

                    insertionSort(
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