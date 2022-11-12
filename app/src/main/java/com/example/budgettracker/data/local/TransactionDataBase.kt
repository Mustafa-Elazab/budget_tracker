package com.example.budgettracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.budgettracker.model.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class TransactionDataBase : RoomDatabase() {

    abstract fun getTransactionDao(): TransactionDao

}