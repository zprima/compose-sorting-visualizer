package com.example.sortingvisualizer.ui.screen

import android.graphics.Paint
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sortingvisualizer.domain.algorithm.Algorithm
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()){
    val uiState = viewModel.uiState
    val scrollState = rememberScrollState()
    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
                       Box(modifier = Modifier
                           .fillMaxWidth()
                           .height(300.dp)
                           .background(Color.Black)){
                           Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                               Text("Pick a sorting algorithm")
                               Divider(Modifier.padding(bottom = 24.dp))

                               Button(onClick = { scope.launch{sheetState.collapse(); viewModel.sort(Algorithm.BUBBLE_SORT)} }) { Text("Bubble Sort") }
                               Button(onClick = { scope.launch{sheetState.collapse(); viewModel.sort(Algorithm.QUICK_SORT)} }) { Text("Quick Sort") }
                               Button(onClick = { scope.launch{sheetState.collapse(); viewModel.sort(Algorithm.MERGE_SORT)} }) { Text("Merge Sort") }
                               Button(onClick = {scope.launch{sheetState.collapse(); viewModel.sort(Algorithm.INSERTION_SORT)} }) { Text("Insertion Sort") }
                           }
                       }
        },
        sheetPeekHeight = 0.dp,
        topBar = {
            TopAppBar(
                title = { Text(uiState.selectedSortAlgorithm.name) },
                actions = {
                    IconButton(onClick = { scope.launch {
                        if (sheetState.isCollapsed)
                            sheetState.expand()
                        else
                            sheetState.collapse()
                    }}) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        },

    ) {

        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { viewModel.reshuffle() }) { Text("Reshuffle") }

                if (uiState.sortRunning) {
                    Button(onClick = { viewModel.stopSort() }) { Text("Stop Sort") }
                }
            }



            Spacer(Modifier.height(16.dp))

            // NumberVisualizer(uiState.numbers)
            NumberVisualizerCircle(uiState.numbers)
        }
    }
}

fun radians(value:Float) = value * Math.PI/180

@Composable
fun NumberVisualizerCircle(numbers: List<Int>){
    BoxWithConstraints() {
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val center = Offset(maxWidthPx / 2, maxHeightPx / 2)

        // val angles = 360 / numbers.size // 3,6 angle per number
        val radiansPerTick = radians(360f / numbers.size)

//        Column(Modifier.verticalScroll(rememberScrollState()).fillMaxWidth()){
//
//
//            numbers.forEachIndexed{ index, number ->
//
//                val maxRange = (maxWidthPx / 2)
//                val numberRange = (maxRange / number) * 100
//
//                val angle = ((index) * angles).toFloat() - (Math.PI / 2).toFloat()
//
//                val secondsStart = center + Offset(cos(angle), sin(angle)) * 60f
//                val secondsEnd = center + Offset(cos(angle), sin(angle)) * numberRange
//
//
//                Text("$number: $angle - $secondsStart, $secondsEnd")
//            }
//        }

        Canvas(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .fillMaxWidth()
        )
        {
            drawCircle(color = Color.Red, radius = 60f, center = center)

            val maxDraw = (maxWidthPx / 2) // 720

            numbers.forEachIndexed { index, number ->
                val angle = (index * radiansPerTick).toFloat() - (Math.PI / 2).toFloat()
                val calculatedEnd =
                    center + Offset(cos(angle),sin(angle)) * (maxDraw*number/numbers.size)

                val color = grafColors.filter { it.key <= number }.values.lastOrNull() ?: Color.White

                drawLine(
                    color = color,
                    start = center,
                    end = calculatedEnd,
                    strokeWidth = 5f
                )
            }
        }
    }
}

val grafColors = mapOf<Int, Color>(
    0 to Color.Red,
    80 to Color.Green,
    160 to Color.Blue,
    240 to Color.Yellow,
    320 to Color.Magenta
)

@Composable
fun NumberVisualizer(numbers: List<Int>) {
    Canvas(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight()
            .fillMaxWidth()
//            .border(1.dp, Color.Blue)

    ){
        val width = size.width
        val height = size.height

        val strokeWidth = Math.ceil((width.toDouble() / numbers.size)).toFloat()

        var start = 0f
        var end = 0f

        numbers.forEachIndexed { index, number ->
            start = index * strokeWidth + (strokeWidth / 2)
            end = height - ((height / numbers.size) * number)

            drawLine(
                color = Color.Red,
                strokeWidth = strokeWidth,
                start = Offset(x=start, y=height),
                end = Offset(x=start, y=end)
            )
        }
    }
}