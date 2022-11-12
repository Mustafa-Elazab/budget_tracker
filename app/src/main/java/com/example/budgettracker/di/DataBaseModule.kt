package com.example.budgettracker.di

import android.content.Context
import androidx.room.Room
import com.example.budgettracker.data.local.TransactionDataBase
import com.example.budgettracker.utils.UIModeDataStore
import com.example.budgettracker.utils.UIModeImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideTransactionDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        TransactionDataBase::class.java,
        "transactions"
    ).build()


    @Singleton
    @Provides
    fun provideDao(database: TransactionDataBase) = database.getTransactionDao()

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context): UIModeImpl {
        return UIModeDataStore(context)
    }





}