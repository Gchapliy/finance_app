package com.example.myapplication.core.data.repository

import com.example.myapplication.core.data.dao.TransactionDao
import com.example.myapplication.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val dao: TransactionDao
) {

    fun getTransactions(): Flow<List<Transaction>> = dao.getAll()

    fun getLast30DaysTransactions(accountId: Long, fromDate: LocalDate): Flow<List<Transaction>> {
        return dao.getLast30DaysTransactions(accountId, fromDate)
    }

    suspend fun addTransaction(transaction: Transaction): Unit = dao.insert(transaction)
}