package com.example.myexpensetracker.di

import android.content.Context
import com.example.myexpensetracker.data.ExpenseTrackerDatabase
import com.example.myexpensetracker.data.dao.ExpenseTrackerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context, ): ExpenseTrackerDatabase {
        return ExpenseTrackerDatabase.getInstance(context)
    }

    @Provides
    fun provideExpenseDao(database: ExpenseTrackerDatabase): ExpenseTrackerDao {
        return database.expenseTrackerDao()
    }
}
