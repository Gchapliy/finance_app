package com.example.myapplication.core.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.domain.model.Transaction
import com.example.myapplication.core.domain.model.TransactionType
import com.example.myapplication.ui.theme.primaryGreen
import com.example.myapplication.ui.theme.primaryRed
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDate

private const val MONEY_DIVIDER = 100

@Composable
fun TransactionItem(transaction: Transaction, currencySign: String, scale: Int) {
    val amount = remember(transaction.amount) {
        BigDecimal(transaction.amount)
            .divide(BigDecimal(MONEY_DIVIDER))
            .setScale(scale, RoundingMode.HALF_EVEN)
    }

    val amountSign = if (amount > BigDecimal.ZERO) "+" else "-"
    val amountColor = if (amount > BigDecimal.ZERO) primaryGreen else primaryRed
    val absoluteAmount = amount.abs()

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(Color.Transparent),
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${transaction.description}",
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "$amountSign $absoluteAmount $currencySign",
                modifier = Modifier.padding(8.dp),
                color = amountColor
            )
        }
    }
}

@Composable
fun TransactionItemSkeleton() {
    val brush = shimmerBrush()

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(Color.Transparent),
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }
    }
}

@Preview(widthDp = 500, heightDp = 60)
@Composable
fun TransactionItemPreview() {
    Surface() {
        TransactionItem(
            transaction = Transaction(
                id = 1,
                accountId = 1,
                categoryId = 1,
                createdAt = Instant.now(),
                description = "Groceries",
                amount = -15000,
                transactionDate = LocalDate.now(),
                transactionType = TransactionType.EXPENSE
            ),
            currencySign = "₴",
            scale = 2
        )
    }

}

@Preview(widthDp = 500, heightDp = 60)
@Composable
fun TransactionItemSkeletonPreview() {
    Surface() {
        TransactionItemSkeleton()
    }
}