package com.example.befine.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class Storage private constructor() {
    companion object {
        @Volatile
        private var instance: Storage? = null
        private var storage: FirebaseStorage = Firebase.storage

        fun getInstance(): Storage {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Storage()
                    }
                }
            }
            return instance!!
        }
    }

    fun getStorage(): FirebaseStorage {
        return storage
    }
}