package com.example.sortingvisualizer.ui.screen

data class HomeUiState(
    val numbers: List<Int> = (1..100).toList(),
    val sortRunning: Boolean = false
)