package com.example.myexpensetracker.addexpense

import androidx.lifecycle.ViewModel
import com.example.myexpensetracker.R
import com.example.myexpensetracker.data.dao.ExpenseTrackerDao
import com.example.myexpensetracker.data.model.ExpenseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel@Inject constructor(private val dao: ExpenseTrackerDao) : ViewModel() {

    suspend fun addExpense(expense: ExpenseEntity) {
        dao.insertExpense(expense)
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
            "Other" -> R.drawable.food

            else -> when (type) {
                "Income" -> R.drawable.income
                else -> R.drawable.expenses
            }

        }
    }
}



