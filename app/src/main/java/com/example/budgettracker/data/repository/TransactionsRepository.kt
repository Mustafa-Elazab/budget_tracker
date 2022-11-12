package com.example.budgettracker.data.repository

import com.example.budgettracker.data.local.TransactionDataBase
import com.example.budgettracker.model.Transaction
import javax.inject.Inject


class TransactionsRepository  @Inject constructor(private val db : TransactionDataBase){

    // insert transaction
    suspend fun insert(transaction: Transaction) = db.getTransactionDao().insertTransaction(
        transaction
    )

    // update transaction
    suspend fun update(transaction: Transaction) = db.getTransactionDao().updateTransaction(
        transaction
    )

    // delete transaction
    suspend fun delete(transaction: Transaction) = db.getTransactionDao().deleteTransaction(
        transaction
    )

    // delete all transactions
    suspend fun deleteAllTransactions() = db.getTransactionDao().deleteAllTransactions()


    // get all transaction
    private fun getAllTransactions() = db.getTransactionDao().getAllTransactions()

    // get single transaction type - Expense or Income or else overall
    fun getAllSingleTransaction(transactionType: String) = if (transactionType == "Overall") {
        getAllTransactions()
    } else {
        db.getTransactionDao().getAllSingleTransaction(transactionType)
    }

    // get transaction by ID
    fun getByID(id: Int) = db.getTransactionDao().getTransactionByID(id)

    // delete transaction by ID
    suspend fun deleteByID(id: Int) = db.getTransactionDao().deleteTransactionByID(id)

}