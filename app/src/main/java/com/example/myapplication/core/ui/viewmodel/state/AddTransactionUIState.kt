package com.example.myapplication.core.ui.viewmodel.state

import com.example.myapplication.core.domain.model.Account
import com.example.myapplication.core.domain.model.TransactionCategory

sealed interface AddTransactionUIState {
    data object Loading: AddTransactionUIState

    data class Success(
        val account: Account,
        val transactionCategories: List<TransactionCategory>
    ): AddTransactionUIState

    data object Error: AddTransactionUIState
}