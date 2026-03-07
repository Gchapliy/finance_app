package com.example.myapplication.core.data.repository

import com.example.myapplication.core.data.dao.AccountDao
import com.example.myapplication.core.domain.model.Account
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val dao: AccountDao
){
    fun getAccounts(): Flow<List<Account>> = dao.getAllAccounts()

    fun getAccountById(id: Long): Flow<Account?> = dao.getAccountById(id)

    fun getBalance(accountId: Long): Flow<Long> = dao.getBalance(accountId)
}