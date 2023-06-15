package com.example.befine.preferences

import android.content.Context
import android.util.Log
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
        var name = stringPreferencesKey("NAME")
        val photo = stringPreferencesKey("PHOTO")
    }

    suspend fun setAuthPreference(data: AuthData) {
        pref.edit {
            it[userId] = data.userId ?: ""
            it[email] = data.email ?: ""
            it[name] = data.name ?: ""
            it[photo] = data.photo ?: ""
        }
    }

    fun getAuthPreference() = pref.data.map {
        AuthData(it[userId] ?: "", it[email] ?: "", name = it[name] ?: "", photo = it[photo] ?: "")
    }


}