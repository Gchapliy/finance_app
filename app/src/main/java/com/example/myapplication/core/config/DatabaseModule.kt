package com.example.myapplication.core.config

import android.content.Context
import androidx.room.Room
import com.example.myapplication.core.data.dao.AccountDao
import com.example.myapplication.core.data.dao.TransactionCategoryDao
import com.example.myapplication.core.data.dao.TransactionDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "expenses_db"
        )
            // TODO: Check if this is the correct way to pre-populate the database with default categories
            .createFromAsset("database/prepopulated_data.db")
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    // insert the data on the IO Thread
//                    CoroutineScope(Dispatchers.IO).launch {
//                        // Pre-populate the database with default categories
//                        val database = provideDatabase(context)
//                        val categoryDao = database.expenseCategoryDao()
//                        categoryDao.insertAll(
//                            listOf(
//                                TransactionCategory(name = "Food"),
//                                TransactionCategory(name = "Transport"),
//                                TransactionCategory(name = "Entertainment"),
//                                TransactionCategory(name = "Utilities"),
//                                TransactionCategory(name = "Health"),
//                                TransactionCategory(name = "Education"),
//                                TransactionCategory(name = "Shopping"),
//                                TransactionCategory(name = "Other")
//                            )
//                        )
//                    }
//                }
//            })
            .build()
    }

    @Provides
    fun provideExpenseDao(db: AppDatabase): TransactionDao {
        return db.expenseDao()
    }

    @Provides
    fun provideExpenseCategoryDao(db: AppDatabase): TransactionCategoryDao {
        return db.expenseCategoryDao()
    }

    @Provides
    fun provideAccountDao(db: AppDatabase): AccountDao {
        return db.accountDao()
    }
}