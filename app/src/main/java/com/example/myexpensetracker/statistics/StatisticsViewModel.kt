package com.example.myexpensetracker.statistics

import androidx.lifecycle.ViewModel
import com.example.myexpensetracker.data.dao.ExpenseTrackerDao
import com.example.myexpensetracker.data.model.ExpenseSummary
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    expenseDao: ExpenseTrackerDao
) : ViewModel() {
    val entries = expenseDao.getExpensesByDate()
    val topEntries = expenseDao.getTopExpenses()

    fun getEntriesForChart(expenses: List<ExpenseSummary>): List<Entry> {
        val list = mutableListOf<Entry>()
        for (entry in expenses) {
            list.add(Entry(entry.date.toFloat(), entry.total_amount.toFloat()))
        }
        return list
    }
}

