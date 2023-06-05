package com.example.befine.repository

import com.example.befine.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UsersRepository(val db: FirebaseFirestore = Firebase.firestore) {
    companion object {
        @Volatile
        private var instance: UsersRepository? = null

        fun getInstance(): UsersRepository {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = UsersRepository()
                    }
                }
            }
            return instance!!
        }
    }

    fun createUser(userId: String, user: UserData, callbackWhenFailed: () -> Unit) {
        db.collection("users").document(userId).set(user).addOnFailureListener {
            callbackWhenFailed()
        }
    }
}