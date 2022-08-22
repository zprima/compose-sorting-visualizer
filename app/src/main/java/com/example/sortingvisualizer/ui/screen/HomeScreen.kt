package com.example.sortingvisualizer.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sortingvisualizer.domain.algorithm.Algorithm
import com.example.sortingvisualizer.domain.algorithm.Visualizer
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()){
    val uiState = viewModel.uiState

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

                               Button(onClick = { scope.launch{sheetState.collapse(); viewModel.selectAlgorithm(Algorithm.BUBBLE_SORT)} }) { Text("Bubble Sort") }
                               Button(onClick = { scope.launch{sheetState.collapse(); viewModel.selectAlgorithm(Algorithm.QUICK_SORT)} }) { Text("Quick Sort") }
                               Button(onClick = { scope.launch{sheetState.collapse(); viewModel.selectAlgorithm(Algorithm.MERGE_SORT)} }) { Text("Merge Sort") }
                               Button(onClick = {scope.launch{sheetState.collapse(); viewModel.selectAlgorithm(Algorithm.INSERTION_SORT)} }) { Text("Insertion Sort") }
                           }
                       }
        },
        sheetPeekHeight = 0.dp,
        topBar = {
//            TopAppBar(
//                title = { Text(uiState.selectedSortAlgorithm.name) },
//                actions = {
//                    IconButton(onClick = { scope.launch {
//                        if (sheetState.isCollapsed)
//                            sheetState.expand()
//                        else
//                            sheetState.collapse()
//                    }}) {
//                        Icon(Icons.Default.Settings, contentDescription = null)
//                    }
//                }
//            )
        },

    ) {
        Column {
            Column(Modifier.fillMaxHeight(2/3f)) {
                Visualizer(uiState.visualizer, uiState.numbers)
            }

            Column(Modifier.fillMaxHeight()) {
                Controls(
                    sheetState,
                    uiState.selectedSortAlgorithm,
                    selectedVisualizer = uiState.visualizer,
                    reshuffleClick = { viewModel.reshuffle() },
                    sortStartClick = { viewModel.sort() },
                    sortStopClick = { viewModel.stopSort() },
                    isSortRunning = uiState.sortRunning,
                    setVisualizer = { viewModel.selectVisualizer(it) }
                )
            }
        }
    }
}

@Composable
fun Visualizer(selectedVisualizer: Visualizer, numbers: List<Int>) {
    when(selectedVisualizer){
        Visualizer.BAR -> NumberVisualizer(numbers)
        Visualizer.SPIRAL -> NumberVisualizerSpiral(numbers)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Controls(
    sheetState: BottomSheetState,
    selectedSortAlgorithm: Algorithm,
    selectedVisualizer: Visualizer,
    reshuffleClick: () -> Unit,
    sortStartClick: ()->Unit,
    sortStopClick:()->Unit,
    isSortRunning: Boolean,
    setVisualizer:(Visualizer)->Unit
) {
    val scope = rememberCoroutineScope()
    var visualizerDropDownState by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()){
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Sorting alg:")

            TextButton(onClick = {
                scope.launch {
                    if(sheetState.isCollapsed){
                        sheetState.expand()
                    } else {
                        sheetState.collapse()
                    }
                }
            }) {
                Text(selectedSortAlgorithm.name)
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Visualizer type:")

            TextButton(onClick = {
                visualizerDropDownState = !visualizerDropDownState
            }) {
                Text(selectedVisualizer.name)
            }
            DropdownMenu(expanded = visualizerDropDownState, onDismissRequest = { visualizerDropDownState = false }) {
                DropdownMenuItem(onClick = { setVisualizer(Visualizer.BAR); visualizerDropDownState = false }) {
                    Text("BAR")
                }
                DropdownMenuItem(onClick = { setVisualizer(Visualizer.SPIRAL); visualizerDropDownState = false }) {
                    Text("SPIRAL")
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            if(!isSortRunning){
                Button(onClick = reshuffleClick) {
                    Text("Reshuffle")
                }

                Button(onClick = sortStartClick) {
                    Text("Sort")
                }

            } else {
                Button(onClick = sortStopClick) {
                    Text("Stop")
                }
            }
        }
    }
}

fun radians(value:Float) = value * Math.PI/180

@Composable
fun NumberVisualizerSpiral(numbers: List<Int>){
    BoxWithConstraints() {
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val maxHeightPx = with(LocalDensity.current) { maxHeight.toPx() }
        val center = Offset(maxWidthPx / 2, maxHeightPx / 2)

        val radiansPerTick = radians(360f / numbers.size)

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
    ){
        val width = size.width
        val height = size.height

        val strokeWidth = Math.ceil((width.toDouble() / numbers.size)).toFloat()

        var start = 0f
        var end = 0f

        numbers.forEachIndexed { index, number ->
            start = index * strokeWidth + (strokeWidth / 2)
            end = height - ((height / numbers.size) * number)

            val color = grafColors.filter { it.key <= number }.values.lastOrNull() ?: Color.White

            drawLine(
                color = color,
                strokeWidth = strokeWidth,
                start = Offset(x=start, y=height),
                end = Offset(x=start, y=end)
            )
        }
    }
}
