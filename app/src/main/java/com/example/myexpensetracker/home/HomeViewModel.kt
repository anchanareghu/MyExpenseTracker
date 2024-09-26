package com.example.myexpensetracker.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myexpensetracker.R
import com.example.myexpensetracker.data.dao.ExpenseTrackerDao
import com.example.myexpensetracker.data.model.ExpenseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val expenseDao: ExpenseTrackerDao) : ViewModel() {
    val expenses = expenseDao.getAllExpenses()
    val selectedCurrency = mutableStateOf("USD")

    fun getBalance(list: List<ExpenseEntity>): String {
        var totalIncome = 0.0
        var currencySymbol = ""
        for (expense in list) {
            val (convertedAmount, symbol) = convertCurrency(expense.amount, selectedCurrency.value)
            currencySymbol = symbol
            if (expense.type == "Income") {
                totalIncome += convertedAmount
            } else {
                totalIncome -= convertedAmount
            }
        }
        return "$currencySymbol ${"%.2f".format(totalIncome)}"
    }

    fun getExpense(list: List<ExpenseEntity>): String {
        var totalExpense = 0.0
        var currencySymbol = ""
        for (expense in list) {
            if (expense.type == "Expense") {
                val (convertedAmount, symbol) = convertCurrency(
                    expense.amount,
                    selectedCurrency.value
                )
                currencySymbol = symbol
                totalExpense += convertedAmount
            }
        }
        return "$currencySymbol ${"%.2f".format(totalExpense)}"
    }

    fun getIncome(list: List<ExpenseEntity>): String {
        var totalIncome = 0.0
        var currencySymbol = ""
        for (income in list) {
            if (income.type == "Income") {
                val (convertedAmount, symbol) = convertCurrency(
                    income.amount,
                    selectedCurrency.value
                )
                currencySymbol = symbol
                totalIncome += convertedAmount
            }
        }
        return "$currencySymbol ${"%.2f".format(totalIncome)}"
    }

    fun getItemIcon(item: ExpenseEntity, type: String): Int {
        return when (item.category) {
            "Netflix" -> R.drawable.netflix
            "Paypal" -> R.drawable.paypal
            "Starbucks" -> R.drawable.starbucks
            "Youtube" -> R.drawable.youtube
            "Upwork" -> R.drawable.upwork
            "Google Pay" -> R.drawable.google_pay
            "Mastercard" -> R.drawable.mastercard
            "Other" -> R.drawable.food
            else -> {
                if (item.title == "Food" || item.title == "Lunch" || item.title == "Dinner" || item.title == "Snacks" || item.title == "Drinks") {
                    R.drawable.food
                } else {
                    when (type) {
                        "Income" -> R.drawable.income
                        else -> R.drawable.expenses
                    }
                }
            }
        }
    }

    fun addItem(item: ExpenseEntity) {
        viewModelScope.launch {
            expenseDao.insertExpense(item)
        }
    }

    fun removeItem(item: ExpenseEntity) {
        viewModelScope.launch {
            expenseDao.deleteExpense(item)
        }
    }

}


fun convertCurrency(amount: Double, currency: String): Pair<Double, String> {
    return when (currency) {
        "USD" -> Pair(amount, "$")
        "EUR" -> Pair(amount * 0.92, "€")
        "GBP" -> Pair(amount * 0.79, "£")
        "INR" -> Pair(amount * 83.97, "₹")

        else -> Pair(amount, "$")
    }
}
