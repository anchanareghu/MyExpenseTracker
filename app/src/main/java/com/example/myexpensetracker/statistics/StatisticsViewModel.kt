package com.example.myexpensetracker.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myexpensetracker.data.dao.ExpenseTrackerDao
import com.example.myexpensetracker.data.model.ExpenseSummary
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val expenseDao: ExpenseTrackerDao
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatisticsUiState>(StatisticsUiState.Loading)
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                expenseDao.getExpensesByDate(),   // Flow<List<ExpenseSummary>>
                expenseDao.getTopExpenses()       // Flow<List<ExpenseEntity>>
            ) { entries, topEntries ->
                if (entries.isNotEmpty() || topEntries.isNotEmpty()) {
                    StatisticsUiState.Success(entries, topEntries)
                } else {
                    StatisticsUiState.Error("No data available")
                }
            }
                .catch { e ->
                    _uiState.value = StatisticsUiState.Error(e.message ?: "Unknown error")
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun getEntriesForChart(expenses: List<ExpenseSummary>): List<Entry> {
        return expenses.map { summary ->
            Entry(summary.date.toFloat(), summary.total_amount.toFloat())
        }
    }
}

