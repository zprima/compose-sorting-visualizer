package com.example.sortingvisualizer.ui.screen

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sortingvisualizer.domain.algorithm.Algorithm

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()){
    val uiState = viewModel.uiState
    val scrollState = rememberScrollState()

    Column(){
        Button(
            onClick = { viewModel.reshuffle() }
        ) {
            Text("Reshuffle")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { viewModel.sort(Algorithm.BUBBLE_SORT) }) {  Text("Bubble Sort") }
            Button(onClick = { viewModel.sort(Algorithm.QUICK_SORT) }) {  Text("Quick Sort") }
        }

        Text("Sort is ${if(uiState.sortRunning) "Running" else "Not Running"}")

        Spacer(Modifier.height(16.dp))

        NumberVisualizer(uiState.numbers)
    }
}

@Composable
fun NumberVisualizer(numbers: List<Int>) {
    val density = LocalDensity.current.density

    Canvas(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(0.7f)
            .fillMaxWidth()
            .border(1.dp, Color.Blue)

    ){
        val width = size.width
        val height = size.height

        val strokeWidth = Math.floor((width.toDouble() / numbers.size)).toFloat()

        var start = 0f
        var end = 0f

        val paint = Paint()
        paint.color = Color.White.hashCode()
        paint.textSize = 32f

        numbers.forEachIndexed { index, number ->
            start = index * strokeWidth + (strokeWidth / 2)
            end = height - ((height / numbers.size) * number)

            drawLine(
                color = if(index%2==0) Color.Black else Color.Red,
                strokeWidth = strokeWidth,
                start = Offset(x=start, y=height),
                end = Offset(x=start, y=end)
            )

            drawContext.canvas.nativeCanvas.drawText(
                number.toString(),
                start,
                height,
                paint
            )
        }
    }
}