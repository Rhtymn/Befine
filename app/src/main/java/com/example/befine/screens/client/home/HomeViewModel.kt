package com.example.befine.screens.client.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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

    private val _searchValue = MutableLiveData("")
    val searchValue: LiveData<String> = _searchValue

    private val _searchedRepairShop = mutableStateListOf<RepairShopWithId>()
    val searchedRepairShop: SnapshotStateList<RepairShopWithId> = _searchedRepairShop

    init {
        getAllRepairShop()
    }

    fun resetSearchValue() {
        _searchValue.value = ""
    }

    fun resetSearchedRepairShop() {
        _searchedRepairShop.clear()
    }

    fun onChangeSearchValue(value: String) {
        _searchValue.value = value
        val searchedResult = _repairShopData.value?.filter {
            value in it.repairShop.name.toString().lowercase()
        }
        if (searchedResult != null) {
            _searchedRepairShop.addAll(searchedResult)
        }

        if (value.isEmpty()) {
            _searchedRepairShop.clear()
        }
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