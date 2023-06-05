package com.example.befine.repository

import com.example.befine.data.RepairShopData
import com.example.befine.model.RepairShop
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepairShopRepository(val db: FirebaseFirestore = Firebase.firestore) {
    fun getRepairShops(): List<RepairShop> {
        return RepairShopData.repairShop
    }

    companion object {
        @Volatile
        private var instance: RepairShopRepository? = null

        fun getInstance(): RepairShopRepository =
            instance ?: synchronized(this) {
                RepairShopRepository().apply {
                    instance = this
                }
            }
    }

    fun createRepairShop(
        repairShop: RepairShopData,
        callbackWhenFailed: () -> Unit
    ) {
        db.collection("repairShops").add(repairShop).addOnFailureListener {
            callbackWhenFailed()
        }
    }
}