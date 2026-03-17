package com.example.myapplication.core.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.core.domain.model.Account
import com.example.myapplication.core.domain.model.TransactionCategory
import com.example.myapplication.core.ui.screen.components.MoneyAmountInput
import com.example.myapplication.core.ui.viewmodel.AddTransactionViewModel
import com.example.myapplication.core.ui.viewmodel.state.AddTransactionUIState
import com.example.myapplication.core.ui.viewmodel.state.TransactionState

@Composable
fun AddTransactionScreen(
    navController: NavController,
    addTransactionViewModel: AddTransactionViewModel = hiltViewModel()
) {
    val transactionState by addTransactionViewModel.transactionState.collectAsStateWithLifecycle()
    val addTransactionUIState by addTransactionViewModel.addTransactionUIState.collectAsStateWithLifecycle()

    when (val mainState = addTransactionUIState) {
        AddTransactionUIState.Loading -> {
            AddTransactionScreenSkeleton()
        }

        AddTransactionUIState.Error -> {
            Text("No account found")
        }

        is AddTransactionUIState.Success -> {
            val successState = addTransactionUIState as AddTransactionUIState.Success
            val account = successState.account
            val categories = successState.transactionCategories

            AddTransactionScreenContent(navController, addTransactionViewModel, transactionState, account, categories)
        }
    }
}


@Composable
fun AddTransactionScreenContent(
    navController: NavController,
    addTransactionViewModel: AddTransactionViewModel,
    transactionState: TransactionState,
    account: Account,
    categories: List<TransactionCategory>
) {
    Column(modifier = Modifier.fillMaxSize()) {
        MoneyAmountInput(
            value = transactionState.amount,
            onValueChange = { addTransactionViewModel.onAmountChanged(it) },
            currencySymbol = account.currencySign,
        )
    }
}

@Composable
fun AddTransactionScreenSkeleton() {


}
