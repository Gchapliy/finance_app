package com.example.myapplication.core.data.converter

import androidx.room.TypeConverter
import com.example.myapplication.core.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromInstant(value: Instant): Long =
        value.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long): Instant =
        Instant.ofEpochMilli(value)

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String =
        date.toString() // ISO-8601: 2026-02-27

    @TypeConverter
    fun toLocalDate(value: String): LocalDate =
        value.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String =
        type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType =
        TransactionType.valueOf(value)
}