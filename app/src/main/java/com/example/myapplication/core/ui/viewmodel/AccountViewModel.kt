package com.example.myapplication.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.data.repository.AccountRepository
import com.example.myapplication.core.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val repository: AccountRepository
) : ViewModel() {
    fun saveAccountId(accountId: Long) {
        viewModelScope.launch {
            preferencesRepository.saveAccountId(accountId)
        }
    }

    fun getAccountBalance(accountId: Long): StateFlow<Long> {
        return repository.getBalance(accountId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)
    }
}