package com.example.myexpensetracker.addexpense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myexpensetracker.R
import com.example.myexpensetracker.data.dao.ExpenseTrackerDao
import com.example.myexpensetracker.data.model.ExpenseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val dao: ExpenseTrackerDao
) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var amount by mutableStateOf("")
        private set

    var category by mutableStateOf("Upwork")
        private set

    var type by mutableStateOf("Income")
        private set

    var currency by mutableStateOf("USD")
        private set

    var date: String by mutableStateOf(System.currentTimeMillis().toString())
        private set

    fun onNameChanged(new: String) { name = new }
    fun onAmountChanged(new: String) { amount = new }
    fun onCategoryChanged(new: String) { category = new }
    fun onTypeChanged(new: String) { type = new }
    fun onCurrencyChanged(new: String) { currency = new }
    fun onDateChanged(new: String) { date = new }

    suspend fun addExpense() {
        dao.insertExpense(
            ExpenseEntity(
                id = 0,
                title = name,
                amount = amount.toDouble(),
                category = category,
                type = type,
                date = date.toString()
            )
        )
    }



    fun getItemIcon(category: String, type: String): Int {
        return when (category) {
            "Netflix" -> R.drawable.netflix
            "Paypal" -> R.drawable.paypal
            "Starbucks" -> R.drawable.starbucks
            "Spotify" -> R.drawable.spotify
            "Youtube" -> R.drawable.youtube
            "Upwork" -> R.drawable.upwork
            "Google Pay" -> R.drawable.google_pay
            "Mastercard" -> R.drawable.mastercard
            "Salary" -> R.drawable.salary
            "Other" -> R.drawable.shopping
            else -> when (type) {
                "Income" -> R.drawable.income
                else -> R.drawable.expenses
            }
        }
    }
}
