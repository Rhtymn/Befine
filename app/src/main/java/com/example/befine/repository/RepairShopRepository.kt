package com.example.befine.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.befine.model.RepairShop
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class RepairShopRepository(val db: FirebaseFirestore = Firebase.firestore) {
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
        repairShop: RepairShop,
        callbackWhenFailed: () -> Unit
    ) {
        db.collection("repairShops").add(repairShop).addOnFailureListener {
            callbackWhenFailed()
        }
    }

    fun getRepairShops(): MutableMap<String, RepairShop> {
        // val result = mutableListOf<RepairShop>()
        var result = mutableMapOf<String, RepairShop>()
        runBlocking {
            try {
                db.collection("repairShops").get().await().map { document ->
                    val repairShop = document.toObject<RepairShop>()
                    // result.add(repairShop)
                    result[document.id] = repairShop
                }
            } catch (e: FirebaseFirestoreException) {
                Log.d("REPAIR_SHOP", e.message.toString())
            }
        }
        return result
    }
}