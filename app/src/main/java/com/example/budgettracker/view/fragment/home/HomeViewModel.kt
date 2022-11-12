package com.example.budgettracker.view.fragment.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.repository.TransactionsRepository
import com.example.budgettracker.model.Transaction
import com.example.budgettracker.utils.DetailState
import com.example.budgettracker.utils.UIModeImpl
import com.example.budgettracker.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepo: TransactionsRepository,
) :
    ViewModel() {


    private val _uiState = MutableStateFlow<ViewState>(ViewState.Loading)
    private val _detailState = MutableStateFlow<DetailState>(DetailState.Loading)

    // UI collect from this stateFlow to get the state updates
    val uiState: StateFlow<ViewState> = _uiState
    val detailState: StateFlow<DetailState> = _detailState

    //trans filter
    val _transactionFilter = MutableStateFlow("Overall")
    val transactionFilter: StateFlow<String> = _transactionFilter



    // insert transaction
    fun insertTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepo.insert(transaction)
    }

    // update transaction
    fun updateTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepo.update(transaction)
    }

    // delete transaction
    fun deleteTransaction(transaction: Transaction) = viewModelScope.launch {
        transactionRepo.delete(transaction)
    }

    // delete transaction
    fun deleteByID(id: Int) = viewModelScope.launch {
        transactionRepo.deleteByID(id)
    }
    // delete all transactions
    fun deleteAllTransactions() = viewModelScope.launch {
        transactionRepo.deleteAllTransactions()
    }

    // get transaction by id
    fun getByID(id: Int) = viewModelScope.launch {
        _detailState.value = DetailState.Loading
        transactionRepo.getByID(id).collect { result: Transaction? ->
            if (result != null) {
                _detailState.value = DetailState.Success(result)
            }
        }
    }

    // get all transaction
    fun getAllTransaction(type: String) = viewModelScope.launch {
        transactionRepo.getAllSingleTransaction(type).collect { result ->
            if (result.isEmpty()) {
                _uiState.value = ViewState.Empty
            } else {
                _uiState.value = ViewState.Success(result)
                Log.i("Filter", "Transaction filter is ${transactionFilter.value}")
            }
        }
    }



    fun allIncome() {
        _transactionFilter.value = "Income"
    }

    fun allExpense() {
        _transactionFilter.value = "Expense"
    }

    fun overall() {
        _transactionFilter.value = "Overall"
    }


}