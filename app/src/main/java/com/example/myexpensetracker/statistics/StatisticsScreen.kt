package com.example.myexpensetracker.statistics

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myexpensetracker.R
import com.example.myexpensetracker.home.HomeViewModel
import com.example.myexpensetracker.home.TransactionList
import com.example.myexpensetracker.ui.theme.MyExpenseTrackerTheme
import com.example.myexpensetracker.utils.Utils.formatDateForChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt
import androidx.core.graphics.toColorInt
import com.example.myexpensetracker.data.model.ExpenseEntity
import com.example.myexpensetracker.data.model.ExpenseSummary
import com.example.myexpensetracker.ui.theme.Purple

@Composable
fun StatisticsScreen(navController: NavController) {
    val viewModel: StatisticsViewModel = hiltViewModel()
    val homeScreenViewModel: HomeViewModel = hiltViewModel()
    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .height(56.dp),
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
                Text(
                    text = "Your Expense Statistics",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is StatisticsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Purple)
                }
            }

            is StatisticsUiState.Success -> {
                val state = uiState as StatisticsUiState.Success
                val entries = viewModel.getEntriesForChart(state.entries)

                Column(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Box(Modifier.padding(16.dp)) {
                        LineChart(entries)
                    }
                    TransactionList(
                        modifier = Modifier,
                        title = "Top Expenses",
                        list = state.topEntries,
                        currency = "USD",
                        viewModel = homeScreenViewModel,
                        snackBarHostState = snackBarHostState
                    )
                }
            }

            is StatisticsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.empty),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .size(250.dp)
                        )
                        Text(
                            text = (uiState as StatisticsUiState.Error).message,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


sealed interface StatisticsUiState {
    object Loading : StatisticsUiState
    data class Success(
        val entries: List<ExpenseSummary>,
        val topEntries: List<ExpenseEntity>
    ) : StatisticsUiState
    data class Error(val message: String) : StatisticsUiState
}

@Composable
fun LineChart(
    entries: List<Entry>
) {
    val context = LocalContext.current

    Box(modifier = Modifier) {

        AndroidView(
            factory = {
                val view =
                    LayoutInflater.from(context).inflate(R.layout.statistics_line_chart, null)
                view
            }, modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { view ->

            val dataSet = LineDataSet(entries, "Expenses").apply {
                color = "#CFDEF3".toColorInt()
                valueTextColor = android.graphics.Color.BLACK
                lineWidth = 3f
                axisDependency = YAxis.AxisDependency.RIGHT
                setDrawFilled(true)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                valueTextSize = 12f

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "$${value.roundToInt()}"
                    }
                }

                setDrawCircles(true)
                setCircleColor("#CFDEF3".toColorInt())
                circleRadius = 2f
                circleHoleColor = android.graphics.Color.WHITE

                val drawable = ContextCompat.getDrawable(context, R.drawable.area_gradient)
                drawable?.let {
                    fillDrawable = it
                }
            }

            val lineChart = view.findViewById<LineChart>(R.id.chart)

            lineChart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return formatDateForChart(value.toLong())
                }
            }

            lineChart.xAxis.apply {
                granularity = 86400000f
                isGranularityEnabled = true
                setLabelCount(5, true)
                position = XAxis.XAxisPosition.BOTTOM
                setAvoidFirstLastClipping(true)
            }

            lineChart.data = LineData(dataSet)
            lineChart.axisLeft.isEnabled = false
            lineChart.axisRight.isEnabled = false
            lineChart.axisRight.setDrawGridLines(false)
            lineChart.axisLeft.setDrawGridLines(false)
            lineChart.xAxis.setDrawGridLines(false)
            lineChart.xAxis.setDrawAxisLine(false)
            lineChart.extraBottomOffset = 16f
            lineChart.description.isEnabled = false
            lineChart.invalidate()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    MyExpenseTrackerTheme {
        val navController = NavController(LocalContext.current)
        StatisticsScreen(navController)
    }
}
