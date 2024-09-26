package com.example.myexpensetracker.user

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
   @ApplicationContext val context: Context
) : ViewModel() {

    var name: String by mutableStateOf(getStoredName(context))
        private set

    fun updateName(newName: String) {
        name = newName
        storeName(context, newName)
    }

    private fun getStoredName(@ApplicationContext context: Context): String {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_name", "") ?: ""
    }

    private fun storeName(@ApplicationContext context: Context, name: String) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_name", name).apply()
    }
}


fun isFirstTimeUser(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean("is_first_time_user", true)
}

fun setFirstTimeUser(context: Context, isFirstTime: Boolean) {
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean("is_first_time_user", isFirstTime).apply()
}
