package com.example.budgettracker.di

import com.example.budgettracker.storage.DataStorage
import com.example.budgettracker.storage.DataStorageImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    abstract fun bindDataStorage(dataStorageImplementation: DataStorageImplementation): DataStorage



}