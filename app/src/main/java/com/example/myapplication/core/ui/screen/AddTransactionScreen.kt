package com.example.myapplication.core.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.core.domain.model.Account
import com.example.myapplication.core.domain.model.TransactionCategory
import com.example.myapplication.core.ui.screen.components.CategoryChooser
import com.example.myapplication.core.ui.screen.components.CategoryChooserSkeleton
import com.example.myapplication.core.ui.screen.components.MoneyAmountInput
import com.example.myapplication.core.ui.screen.components.MoneyAmountInputSkeleton
import com.example.myapplication.core.ui.viewmodel.AddTransactionViewModel
import com.example.myapplication.core.ui.viewmodel.state.AddTransactionUIState
import com.example.myapplication.core.ui.viewmodel.state.TransactionState
import kotlin.collections.emptyList

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

            AddTransactionScreenContent(
                navController,
                addTransactionViewModel,
                transactionState,
                account,
                categories
            )
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
    var selectedCategory by remember { mutableStateOf<TransactionCategory?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        MoneyAmountInput(
            value = transactionState.amount,
            onValueChange = { addTransactionViewModel.onAmountChanged(it) },
            currencySymbol = account.currencySign,
        )
//        TODO: fix list of categories from db and function addCategory
        CategoryChooser(
            categories,
            selectedCategory,
            { selectedCategory = it })
    }
}

@Composable
fun AddTransactionScreenSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        MoneyAmountInputSkeleton()
        CategoryChooserSkeleton()
    }
}

@Preview(showBackground = true)
@Composable
private fun AddTransactionScreenContentPreview() {
    var selectedCategory by remember { mutableStateOf<TransactionCategory?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        MoneyAmountInput(
            value = "",
            onValueChange = { },
            currencySymbol = "$",
        )
        CategoryChooser(emptyList(), selectedCategory, {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AddTransactionScreenSkeletonPreview() {
    AddTransactionScreenSkeleton()
}
