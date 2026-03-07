package com.example.myapplication.core.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.core.domain.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<Account>>

    @Query("""
    SELECT initialBalance + IFNULL(SUM(amount), 0)
    FROM accounts
    LEFT JOIN transactions
        ON accounts.id = transactions.accountId
    WHERE accounts.id = :accountId
""")
    fun getBalance(accountId: Long): Flow<Long>
    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getAccountById(id: Long): Flow<Account?>
}