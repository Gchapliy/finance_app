package com.example.myapplication.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAll(): Flow<List<Transaction>>

    @Query("""
    SELECT * FROM transactions
    WHERE accountId = :accountId
      AND transactionDate >= :fromDate
    ORDER BY transactionDate DESC, createdAt DESC
""")
    fun getLast30DaysTransactions(
        accountId: Long,
        fromDate: LocalDate
    ): Flow<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)
}