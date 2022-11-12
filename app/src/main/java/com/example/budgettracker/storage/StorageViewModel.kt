/*
 * Written and Developed by Inuwa Ibrahim - https://linktr.ee/Ibrajix
*/

package com.example.budgettracker.storage

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class StorageViewModel @Inject constructor(private val dataStorage: DataStorage) : ViewModel()  {


    // get ui mode
    val getUIMode = dataStorage.selectedTheme

    // save ui mode
    fun setDarkMode(isNightMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorage.setSelectedTheme(isNightMode)
        }
    }


    val isUsersFirstTime = dataStorage.isUserFirstTime().asLiveData()
    fun changeUsersFirstTime(isFirstTime: Boolean){
        viewModelScope.launch {
            dataStorage.setUserFirstTime(isFirstTime)
        }
    }

    private val _hasClickedRetryButton = MutableLiveData<Boolean>()
    val hasClickedRetryButton: LiveData<Boolean> = _hasClickedRetryButton

    fun userClickedRetryButton(isClicked: Boolean) {
        _hasClickedRetryButton.value = isClicked
    }

    fun saveString(key:String,value: String) {
        viewModelScope.launch {
            dataStorage.putString(key, value)
        }
    }

    fun getString(key:String): String? = runBlocking {
        dataStorage.getString(key)
    }

    fun saveInt(key:String,value: Int) {
        viewModelScope.launch {
            dataStorage.putInt(key, value)
        }
    }

    fun getInt(key:String): Int? = runBlocking {
        dataStorage.getInt(key)
    }

    fun saveBoolean(key:String,value: Boolean) {
        viewModelScope.launch {
            dataStorage.putBoolean(key, value)
        }
    }

    fun getBoolean(key:String): Boolean? = runBlocking {
        dataStorage.getBoolean(key)
    }

}