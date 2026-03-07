package com.example.myapplication.core.ui.viewmodel.state

import com.example.myapplication.core.domain.model.Account
import com.example.myapplication.core.domain.model.Transaction
import java.time.LocalDate

sealed interface MainUiState {

    data object Loading : MainUiState

    data class Success(
        val account: Account,
        val transactions: Map<LocalDate, List<Transaction>>
    ) : MainUiState

    data object Empty : MainUiState
}