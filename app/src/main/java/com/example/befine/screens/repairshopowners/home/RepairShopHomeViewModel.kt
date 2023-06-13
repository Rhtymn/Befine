package com.example.befine.screens.repairshopowners.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.befine.firebase.Auth
import com.example.befine.model.AuthData
import com.example.befine.model.User
import com.example.befine.preferences.PreferenceDatastore
import com.example.befine.screens.client.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RepairShopHomeViewModel : ViewModel() {
    private val auth: FirebaseAuth = Auth.getInstance().getAuth()
    private val db: FirebaseFirestore = Firebase.firestore

    suspend fun setUserPreferences(context: Context) {
        val preferenceDatastore = PreferenceDatastore(context = context)
        val user =
            db.collection("users").document(auth.currentUser?.uid!!).get().await().toObject<User>()
        val authData =
            AuthData(
                userId = auth.currentUser?.uid!!,
                email = auth.currentUser?.email!!,
                name = user?.name!!
            )
        Log.d(HomeViewModel.TAG, authData.toString())
        preferenceDatastore.setAuthPreference(authData)
    }
}