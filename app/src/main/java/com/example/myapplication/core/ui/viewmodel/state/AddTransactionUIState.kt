package com.example.myapplication.core.ui.viewmodel.state

import java.time.LocalDate

data class AddTransactionUiState(
    val amount: String = "",
    val description: String = "",
    val categoryId: Long? = null,
    val date: LocalDate = LocalDate.now(),
    val isSaving: Boolean = false,
    val error: String? = null
)