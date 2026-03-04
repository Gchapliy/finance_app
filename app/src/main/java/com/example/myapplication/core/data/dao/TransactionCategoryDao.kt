package com.example.myapplication.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.core.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionCategoryDao {

    @Query("SELECT * FROM transaction_categories ORDER BY name ASC")
    fun getAll(): Flow<List<TransactionCategory>>

    @Insert
    suspend fun insert(category: TransactionCategory)

    @Insert
    suspend fun insertAll(categories: List<TransactionCategory>)

    @Delete
    suspend fun delete(category: TransactionCategory)
}