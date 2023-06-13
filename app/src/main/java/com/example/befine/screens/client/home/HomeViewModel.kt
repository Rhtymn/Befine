package com.example.befine.screens.client.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.firebase.Auth
import com.example.befine.model.AuthData
import com.example.befine.model.RepairShopWithId
import com.example.befine.model.User
import com.example.befine.preferences.PreferenceDatastore
import com.example.befine.repository.RepairShopRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class HomeViewModel(private val repository: RepairShopRepository) : ViewModel() {
    private val auth: FirebaseAuth = Auth.getInstance().getAuth()
    private val db: FirebaseFirestore = Firebase.firestore
    private val _repairShopData = MutableLiveData(listOf<RepairShopWithId>())
    val repairShopData: LiveData<List<RepairShopWithId>> = _repairShopData

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllRepairShop()
    }

    fun getAllRepairShop() {
        _isLoading.value = true
        val datas = repository.getRepairShops()
        val listOfRepairShop = mutableListOf<RepairShopWithId>()
        datas.forEach { entry ->
            listOfRepairShop.add(RepairShopWithId(id = entry.key, repairShop = entry.value))
        }
        _repairShopData.value = listOfRepairShop
        _isLoading.value = false
    }

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
        Log.d(TAG, authData.toString())
        preferenceDatastore.setAuthPreference(authData)
    }

    companion object {
        const val TAG = "HOME_VIEW_MODEL"
    }
}