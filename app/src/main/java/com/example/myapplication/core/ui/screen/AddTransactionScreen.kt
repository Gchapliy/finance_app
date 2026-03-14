package com.example.myapplication.core.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.core.ui.screen.components.MoneyAmountInput
import com.example.myapplication.core.ui.viewmodel.AddTransactionViewModel

@Composable
fun AddTransactionScreen(
    navController: NavController,
    abstractionViewModel: AddTransactionViewModel = hiltViewModel()
) {
    val state by abstractionViewModel.uiState.collectAsStateWithLifecycle()
    val account by abstractionViewModel.account.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        MoneyAmountInput(
            value           = state.amount,
            onValueChange   = { abstractionViewModel.onAmountChanged(it) },
            currencySymbol  = account?.currencySign ?: "$",
        )
    }
}

