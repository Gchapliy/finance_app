package com.example.myapplication.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.data.repository.AccountRepository
import com.example.myapplication.core.data.repository.PreferencesRepository
import com.example.myapplication.core.data.repository.TransactionRepository
import com.example.myapplication.core.domain.model.Transaction
import com.example.myapplication.core.ui.viewmodel.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val DEFAULT_ACCOUNT_ID = 1L

    @OptIn(ExperimentalCoroutinesApi::class)
    val mainState: StateFlow<MainUiState> =
        preferencesRepository.getAccountId()
            .flatMapLatest { id ->
                if (id == null) saveAccountId(DEFAULT_ACCOUNT_ID)

                combine(
                    accountRepository.getAccountById(id ?: DEFAULT_ACCOUNT_ID),
                    getTransactionsForAccount(
                        id ?: DEFAULT_ACCOUNT_ID,
                    )
                ) { account, transactions ->
                    if (account == null) {
                        MainUiState.Empty
                    } else {
                        MainUiState.Success(account, transactions)
                    }
                }
            }
            .onStart {
                emit(MainUiState.Loading)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                MainUiState.Loading
            )

    fun saveAccountId(accountId: Long) {
        viewModelScope.launch {
            preferencesRepository.saveAccountId(accountId)
        }
    }

    fun getTransactionsForAccount(accountId: Long): StateFlow<Map<LocalDate, List<Transaction>>> {
        val fromDate = LocalDate.now().minusDays(30)

        return transactionRepository.getLast30DaysTransactions(accountId, fromDate)
            .map { transactions ->
                transactions.groupBy { it.transactionDate }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
    }
}