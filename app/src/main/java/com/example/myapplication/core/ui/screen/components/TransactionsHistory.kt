package com.example.myapplication.core.ui.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.example.myapplication.core.domain.model.Transaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun TransactionsHistory(transactions: Map<LocalDate, List<Transaction>>, currencySign: String, scale: Int) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    LazyColumn() {
        transactions.forEach { (date, transactions) ->

            // Date header
            item(key = "header_$date") {
                DateItem(date.format(formatter))
            }

            items(transactions.size) { index ->
                AnimatedVisibility(visible = true) {
                    TransactionItem(transaction = transactions[index], currencySign = currencySign, scale = scale)
                }
            }
        }
    }
}