package com.example.myapplication.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.data.repository.TransactionRepository
import com.example.myapplication.core.domain.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    fun getTransactionsForAccount(accountId: Long): StateFlow<Map<LocalDate, List<Transaction>>> {
        val fromDate = LocalDate.now().minusDays(30)

        return repository.getLast30DaysTransactions(accountId, fromDate)
            .map { transactions ->
                transactions.groupBy { it.transactionDate }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
    }
}