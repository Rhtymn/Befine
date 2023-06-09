package com.example.befine.screens.client.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.firebase.Storage
import com.example.befine.model.AuthData
import com.example.befine.model.RepairShop
import com.example.befine.preferences.PreferenceDatastore
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await

class RepairShopDetailsViewModel : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage: FirebaseStorage = Storage.getInstance().getStorage()

    private val _state = MutableLiveData(RepairShopDetailsState())
    val state: LiveData<RepairShopDetailsState> = _state

    private val _userPreference = MutableLiveData(AuthData())
    val userPreference: LiveData<AuthData> = _userPreference

    suspend fun getUserPreference(context: Context) {
        val userPreferenceDatastore = PreferenceDatastore(context)
        userPreferenceDatastore.getAuthPreference().collect {
            _userPreference.postValue(it)
        }
    }

    suspend fun getInitialData(repairShopId: String) {
        // Get repair shop data
        val tempRepairShop = db.collection("repairShops").document(repairShopId).get().await()
            .toObject<RepairShop>() ?: RepairShop()

        _state.value = _state.value?.copy(repairShop = tempRepairShop)

        // Get repair shop image
        val imageRef = storage.reference.child("images/${tempRepairShop.photo}")
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            _state.value = _state.value?.copy(imageUri = uri)
        }

        // Update location
        val location =
            LatLng(tempRepairShop.latitude?.toDouble()!!, tempRepairShop.longitude?.toDouble()!!)
        _state.value = _state.value?.copy(location = location)
    }
}