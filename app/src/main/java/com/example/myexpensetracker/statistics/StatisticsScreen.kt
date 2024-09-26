package com.example.myexpensetracker.statistics

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun StatisticsScreen(navController: NavController) {
    val viewModel: StatisticsViewModel = hiltViewModel()
    val homeScreenViewModel: HomeViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                    text = "Statistics",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    ) {
        val expenses = viewModel.entries.collectAsState(initial = emptyList())
        val topExpenses = viewModel.topEntries.collectAsState(initial = emptyList())
        val entries = viewModel.getEntriesForChart(expenses.value)

        if (topExpenses.value.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(it)
            ) {
                Box(Modifier.padding(16.dp)) {
                    LineChart(entries)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TransactionList(
                    modifier = Modifier,
                    title = "Top Expenses",
                    list = topExpenses.value,
                    currency = "USD",
                    viewModel = homeScreenViewModel,
                    snackbarHostState = snackbarHostState
                )

            }
        } else {
            Box(
                Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.empty),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(250.dp)
                )
            }
        }
    }
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
                color = android.graphics.Color.parseColor("#CFDEF3")
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
                setCircleColor(android.graphics.Color.parseColor("#CFDEF3"))
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
            }

            lineChart.data = LineData(dataSet)
            lineChart.axisLeft.isEnabled = false
            lineChart.axisRight.isEnabled = false
            lineChart.axisRight.setDrawGridLines(false)
            lineChart.axisLeft.setDrawGridLines(false)
            lineChart.xAxis.setDrawGridLines(false)
            lineChart.xAxis.setDrawAxisLine(false)
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
