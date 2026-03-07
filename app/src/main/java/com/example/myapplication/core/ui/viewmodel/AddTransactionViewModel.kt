package com.example.myapplication.core.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.data.repository.AccountRepository
import com.example.myapplication.core.data.repository.TransactionRepository
import com.example.myapplication.core.domain.model.Transaction
import com.example.myapplication.core.domain.model.TransactionType
import com.example.myapplication.core.ui.viewmodel.state.AddTransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel(){
    private val accountId: Long =
        savedStateHandle["accountId"]!!
    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState

    fun onAmountChanged(newAmount: String) {
        _uiState.update { it.copy(amount = newAmount) }
    }

    val account = accountRepository.getAccountById(accountId)

    fun saveTransaction() {
        val current = _uiState.value

        val amountLong = parseToMinorUnits(current.amount)

        viewModelScope.launch {
            transactionRepository.insertTransaction(
                Transaction(
                    accountId = accountId,
                    amount = amountLong,
                    categoryId = current.categoryId!!,
                    description = current.description.takeIf { it.isNotBlank() },
                    transactionDate = current.date,
                    createdAt = Instant.now(),
                    transactionType = if (amountLong > 0) TransactionType.INCOME else TransactionType.EXPENSE
                )
            )
        }
    }

    private fun parseToMinorUnits(amount: String): Long {
        return amount.toBigDecimalOrNull()?.multiply(BigDecimal(100))?.toLong() ?: 0L
    }
}