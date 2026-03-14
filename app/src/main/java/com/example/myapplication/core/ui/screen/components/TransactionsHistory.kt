package com.example.myapplication.core.ui.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.domain.model.Transaction
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun TransactionsHistory(
    transactions: Map<LocalDate, List<Transaction>>,
    currencySign: String,
    scale: Int
) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    LazyColumn() {
        transactions.forEach { (date, transactions) ->

            // Date header
            item(key = "header_$date") {
                DateItem(date.format(formatter))
            }

            items(transactions.size) { index ->
                AnimatedVisibility(visible = true) {
                    TransactionItem(
                        transaction = transactions[index],
                        currencySign = currencySign,
                        scale = scale
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionsHistorySkeleton() {
    val brush = shimmerBrush()
    val list = listOf(1..10)

    LazyColumn(modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Date header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(), // This is the missing piece!
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
        }

        // items
        items(12) { index ->
            TransactionItemSkeleton()
        }
    }
}