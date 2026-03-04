package com.example.myapplication.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val initialBalance: Long,
    val scale: Int,
    val currency: String,
    val currencySign: String
)