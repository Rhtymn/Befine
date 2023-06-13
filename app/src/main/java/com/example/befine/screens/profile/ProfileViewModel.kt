package com.example.befine.screens.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.model.AuthData
import com.example.befine.preferences.PreferenceDatastore

class ProfileViewModel : ViewModel() {
    private val _state = MutableLiveData(AuthData())
    val state: LiveData<AuthData> = _state
    suspend fun getUserPreference(context: Context) {
        val userPreferenceDatastore = PreferenceDatastore(context)
        userPreferenceDatastore.getAuthPreference().collect {
            _state.postValue(it)
        }
    }

    suspend fun deleteUserPreference(context: Context) {
        val userPreferenceDatastore = PreferenceDatastore(context)
        userPreferenceDatastore.setAuthPreference(AuthData())
    }
}