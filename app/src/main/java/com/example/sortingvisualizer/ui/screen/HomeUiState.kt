package com.example.sortingvisualizer.ui.screen

data class HomeUiState(
    val numbers: List<Int> = (1..20).toList(),
    val sortRunning: Boolean = false
)