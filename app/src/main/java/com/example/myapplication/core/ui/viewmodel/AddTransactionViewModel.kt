package com.example.myapplication.core.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.data.repository.AccountRepository
import com.example.myapplication.core.data.repository.TransactionExpenseCategoryRepository
import com.example.myapplication.core.data.repository.TransactionRepository
import com.example.myapplication.core.domain.model.Transaction
import com.example.myapplication.core.domain.model.TransactionType
import com.example.myapplication.core.ui.viewmodel.state.AddTransactionUIState
import com.example.myapplication.core.ui.viewmodel.state.TransactionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val transactionExpenseCategoryRepository: TransactionExpenseCategoryRepository
) : ViewModel() {
    val accountId: Long = checkNotNull(savedStateHandle["accountId"])
    private val _transactionState = MutableStateFlow(TransactionState())
    val transactionState: StateFlow<TransactionState> = _transactionState

    @OptIn(ExperimentalCoroutinesApi::class)
    val addTransactionUIState: StateFlow<AddTransactionUIState> =
        accountRepository.getAccountById(accountId)
            .flatMapLatest { account ->
                if (account == null) flowOf(AddTransactionUIState.Error)
                else transactionExpenseCategoryRepository.getCategories()
                    .map { categories -> AddTransactionUIState.Success(account, categories) }
            }
            .onStart {
                emit(AddTransactionUIState.Loading)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                AddTransactionUIState.Loading
            )

    fun onAmountChanged(newAmount: String) {
        _transactionState.update { it.copy(amount = newAmount) }
    }

    fun saveTransaction() {
        val current = _transactionState.value

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