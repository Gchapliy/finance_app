package com.example.myapplication.core.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.core.data.converter.Converters
import com.example.myapplication.core.data.dao.AccountDao
import com.example.myapplication.core.data.dao.TransactionCategoryDao
import com.example.myapplication.core.data.dao.TransactionDao
import com.example.myapplication.core.domain.model.Account
import com.example.myapplication.core.domain.model.Transaction
import com.example.myapplication.core.domain.model.TransactionCategory

@Database(entities = [Transaction::class, TransactionCategory::class, Account::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): TransactionDao

    abstract fun expenseCategoryDao(): TransactionCategoryDao

    abstract fun accountDao(): AccountDao
}