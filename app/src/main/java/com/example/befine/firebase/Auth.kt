package com.example.befine.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth private constructor() {

    companion object {
        @Volatile
        private var instance: Auth? = null
        private var auth: FirebaseAuth = Firebase.auth

        fun getInstance(): Auth {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Auth()
                    }
                }
            }
            return instance!!
        }
    }

    fun getAuth(): FirebaseAuth {
        return auth
    }
}