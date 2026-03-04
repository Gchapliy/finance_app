package com.example.myapplication.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_categories")
data class TransactionCategory (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val icon: String, // emoji or icon name

    val colorHex: String
)