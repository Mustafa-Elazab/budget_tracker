package com.example.budgettracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.budgettracker.model.Transaction

@Dao
interface TransactionDao {

    // used to insert new transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    // used to update existing transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTransaction(transaction: Transaction)

    // used to delete transaction
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    //delete all transactions
    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

    // get all saved transaction list
    @Query("SELECT * FROM transactions ORDER by createdAt DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    // get all income or expense list by transaction type param
    @Query("SELECT * FROM transactions WHERE transactionType == :transactionType ORDER by createdAt DESC")
    fun getAllSingleTransaction(transactionType: String): Flow<List<Transaction>>

    // get single transaction by id
    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionByID(id: Int): Flow<Transaction>

    // delete transaction by id
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionByID(id: Int)
}