package com.example.sortingvisualizer.ui.screen

import com.example.sortingvisualizer.domain.algorithm.Algorithm
import com.example.sortingvisualizer.domain.algorithm.Visualizer

data class HomeUiState(
    val numbers: List<Int> = (1..360).toList(),
    val sortRunning: Boolean = false,
    val selectedSortAlgorithm: Algorithm = Algorithm.BUBBLE_SORT,
    val visualizer: Visualizer = Visualizer.BAR
)