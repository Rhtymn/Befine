package com.example.befine.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.firebase.Auth
import com.example.befine.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Auth.getInstance().getAuth()
    private val db: FirebaseFirestore = Firebase.firestore
    private val _state = MutableLiveData(LoginState())
    val state: LiveData<LoginState> = _state

    fun onChangeEmailInput(value: String) {
        if (_state.value?.isFailed!!) resetFormState()
        _state.value = _state.value?.copy(email = value)
    }

    fun onChangePasswordInput(value: String) {
        if (_state.value?.isFailed!!) resetFormState()
        _state.value = _state.value?.copy(password = value)
    }

    fun resetFormState() {
        _state.value = _state.value?.copy(isFailed = false, formErrorMsg = "")
    }

    fun resetInputField() {
        _state.value = _state.value?.copy(
            email = "",
            isEmailError = false,
            emailErrorMsg = "",
            password = "",
            isPasswordError = false,
            passwordErrorMsg = ""
        )
    }

    private fun resetErrorFieldState() {
        _state.value = _state.value?.copy(isEmailError = false, isPasswordError = false)
    }

    fun loginHandler(onSuccess: (role: String) -> Unit, onFailure: () -> Unit) {
        try {
            resetErrorFieldState()
            _state.value = _state.value?.copy(isLoading = true)

            emailValidation(_state.value?.email!!, callbackWhenEmpty = {
                _state.value = _state.value?.copy(
                    isEmailError = true,
                    emailErrorMsg = InputFieldError.REQUIRED
                )
            }, callbackWhenInvalidFormat = {
                _state.value = _state.value?.copy(
                    isEmailError = true,
                    emailErrorMsg = EmailError.INVALID_FORMAT
                )
            })

            passwordValidation(_state.value?.password!!, callbackWhenEmpty = {
                _state.value = _state.value?.copy(
                    isPasswordError = true,
                    passwordErrorMsg = InputFieldError.REQUIRED
                )
            }, callbackWhenLessThanEightChar = {
                _state.value = _state.value?.copy(
                    isPasswordError = true,
                    passwordErrorMsg = PasswordError.MIN_CHARS
                )
            })

            // Checking error availability
            if (!_state.value?.isEmailError!! && !_state.value?.isPasswordError!!) {

                // Sign in process
                auth.signInWithEmailAndPassword(_state.value?.email!!, _state.value?.password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            resetInputField()

                            db.collection("users").document(auth.currentUser!!.uid).get()
                                .addOnSuccessListener {
                                    onSuccess(it.get("role") as String)
                                }.addOnFailureListener {
                                    onFailure()
                                }
                        } else {
                            // If sign in fails, display a message to the user
                            val formErrorMsg =
                                if (task.exception is FirebaseAuthInvalidUserException || task.exception is FirebaseAuthInvalidCredentialsException) {
                                    LoginError.INVALID
                                } else {
                                    LoginError.DEFAULT
                                }
                            _state.value =
                                _state.value?.copy(isFailed = true, formErrorMsg = formErrorMsg)
                        }
                        _state.value = _state.value?.copy(isLoading = false)
                    }
            } else {
                _state.value = _state.value?.copy(isLoading = false)
            }
        } catch (e: Exception) {
            Log.d("LOGIN", e.message.toString())
        }
    }
}