package com.example.befine.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.befine.model.AuthData
import kotlinx.coroutines.flow.map
val Context.datastore: DataStore<Preferences> by preferencesDataStore("AUTH_PREFERENCE")

class PreferenceDatastore(context: Context) {
    var pref = context.datastore

    companion object {
        var userId = stringPreferencesKey("USER_ID")
        var email = stringPreferencesKey("EMAIL")
    }

    suspend fun setAuthPreference(data: AuthData) {
        pref.edit {
            it[userId] = data.userId
            it[email] = data.email
        }
    }

    fun getAuthPreference() = pref.data.map {
        AuthData(it[userId] ?: "", it[email] ?: "")
    }

}