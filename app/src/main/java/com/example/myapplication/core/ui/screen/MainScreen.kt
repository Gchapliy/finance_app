package com.example.myapplication.core.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.R
import com.example.myapplication.core.domain.model.Transaction
import com.example.myapplication.core.ui.screen.components.BalanceCard
import com.example.myapplication.core.ui.screen.components.TransactionsHistory
import com.example.myapplication.core.ui.screen.components.TransparentButton
import com.example.myapplication.core.ui.viewmodel.AccountViewModel
import com.example.myapplication.core.ui.viewmodel.TransactionViewModel
import java.time.Instant
import java.time.LocalDate

@Composable
fun MainScreen(
    accountViewModel: AccountViewModel = hiltViewModel(),
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {
    val account = accountViewModel.getAccountById(1)
    val balance by accountViewModel.getAccountBalance(1).collectAsStateWithLifecycle()
    val transactions by transactionViewModel.getTransactionsForAccount(1)
        .collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .weight(0.85f),
        ) {
            BalanceCard(balance, account.currencySign, account.scale)
            TransactionsHistory(transactions, account.currencySign, account.scale)
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .weight(0.15f),
        ) {
            TransparentButton(onClick = {
//            TODO: Implement navigation to AddTransactionScreen
            }, width = 200.dp, height = 60.dp, paddingValues = PaddingValues(16.dp)) {
                Text(stringResource(R.string.add_transaction))
            }
        }
    }
}

@Preview(widthDp = 600, heightDp = 800)
@Composable
fun MainScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(0.85f)
                .padding(24.dp)
        ) {
            BalanceCard(1234567, "₴", 2)
            TransactionsHistory(
                mapOf(
                    LocalDate.of(2026, 3, 1) to listOf(
                        Transaction(
                            id = 1,
                            accountId = 1,
                            categoryId = 1,
                            createdAt = Instant.now(),
                            description = "Groceries",
                            amount = 15000,
                            transactionDate = LocalDate.now()
                        ),
                        Transaction(
                            id = 2,
                            accountId = 1,
                            categoryId = 2,
                            createdAt = Instant.now(),
                            description = "Salary",
                            amount = 500000,
                            transactionDate = LocalDate.now()
                        )
                    ),
                    LocalDate.of(2026, 2, 28) to listOf(
                        Transaction(
                            id = 3,
                            accountId = 1,
                            categoryId = 3,
                            createdAt = Instant.now(),
                            description = "Utilities",
                            amount = 20000,
                            transactionDate = LocalDate.now()
                        ),
                        Transaction(
                            id = 3,
                            accountId = 1,
                            categoryId = 3,
                            createdAt = Instant.now(),
                            description = "Utilities",
                            amount = 20000,
                            transactionDate = LocalDate.now()
                        ),
                        Transaction(
                            id = 4,
                            accountId = 1,
                            categoryId = 3,
                            createdAt = Instant.now(),
                            description = "Utilities",
                            amount = -25000,
                            transactionDate = LocalDate.now()
                        ),
                        Transaction(
                            id = 4,
                            accountId = 1,
                            categoryId = 3,
                            createdAt = Instant.now(),
                            description = "Utilities",
                            amount = -25000,
                            transactionDate = LocalDate.now()
                        ), Transaction(
                            id = 3,
                            accountId = 1,
                            categoryId = 3,
                            createdAt = Instant.now(),
                            description = "Utilities",
                            amount = 20000,
                            transactionDate = LocalDate.now()
                        ),
                        Transaction(
                            id = 4,
                            accountId = 1,
                            categoryId = 3,
                            createdAt = Instant.now(),
                            description = "Utilities",
                            amount = -25000,
                            transactionDate = LocalDate.now()
                        ),
                        Transaction(
                            id = 3,
                            accountId = 1,
                            categoryId = 3,
                            createdAt = Instant.now(),
                            description = "Utilities",
                            amount = 20000,
                            transactionDate = LocalDate.now()
                        ),
                        Transaction(
                            id = 4,
                            accountId = 1,
                            categoryId = 3,
                            createdAt = Instant.now(),
                            description = "Utilities",
                            amount = -25000,
                            transactionDate = LocalDate.now()
                        )
                    )
                ), "₴", 2
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.15f)
        ) {
            TransparentButton(onClick = {
//            TODO: Implement navigation to AddTransactionScreen
            }, width = 200.dp, height = 60.dp, paddingValues = PaddingValues(16.dp)) {
                Text(stringResource(R.string.add_transaction))
            }
        }
    }
}

