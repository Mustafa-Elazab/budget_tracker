package com.example.budgettracker.utils

import com.example.budgettracker.model.Transaction

sealed class DetailState {
    object Loading : DetailState()
    object Empty : DetailState()
    data class Success(val transaction: Transaction) : DetailState()
    data class Error(val exception: Throwable) : DetailState()
}
