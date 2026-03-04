package com.example.myapplication.core.domain.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate

@Entity(tableName = "transactions",
    indices = [
        Index(value = ["accountId"]),
        Index(value = ["accountId", "transactionDate"]),
        Index(value = ["transactionDate"]),
        Index(value = ["categoryId"]),
        Index(value = ["categoryId", "transactionDate"])
    ])
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val amount: Long, // storing amount in cents to avoid floating point issues

    val accountId: Long,

    val categoryId: Long,

    val description: String? = null,

    val createdAt: Instant,

    val updatedAt: Instant? = null,

    val transactionDate: LocalDate,

    val transactionType: TransactionType,

    val isDeleted: Boolean = false
)