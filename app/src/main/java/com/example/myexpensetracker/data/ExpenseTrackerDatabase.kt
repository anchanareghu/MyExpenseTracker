package com.example.myexpensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myexpensetracker.data.dao.ExpenseTrackerDao
import com.example.myexpensetracker.data.model.ExpenseEntity
import javax.inject.Singleton

@Database(entities = [ExpenseEntity::class], version = 5, exportSchema = false)
@Singleton
abstract class ExpenseTrackerDatabase : RoomDatabase() {
    abstract fun expenseTrackerDao(): ExpenseTrackerDao

    companion object {
        private const val DATABASE_NAME = "expense_tracker"

        @Volatile
        private var INSTANCE: ExpenseTrackerDatabase? = null

        fun getInstance(context: Context): ExpenseTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseTrackerDatabase::class.java,
                    DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}