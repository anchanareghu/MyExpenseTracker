package com.example.myexpensetracker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    fun formatDateForCalender(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatDateForChart(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }
    fun formatDayMonthYear(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatStringDateToMonthDayYear(date: String): String {
        val millis = getMillisFromDate(date)
        return formatDayMonthYear(millis)
    }

    fun getMillisFromDate(date: String): Long {
        return getMilliFromDate(date)
    }

    fun getMilliFromDate(dateString: String, dateFormat: String = "yyyy-MM-dd"): Long {
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        return try {
            val date = sdf.parse(dateString)
            date?.time ?: 0L
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

}
