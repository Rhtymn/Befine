package com.example.befine.screens.register.repairshop

import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.firebase.Auth
import com.example.befine.model.RepairShop
import com.example.befine.model.Schedule
import com.example.befine.model.UserData
import com.example.befine.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepairShopSignUpViewModel() : ViewModel() {
    private val auth: FirebaseAuth = Auth.getInstance().getAuth()
    private val db: FirebaseFirestore = Firebase.firestore
    private val _state = MutableLiveData(RepairShopSignUpState())
    val state: LiveData<RepairShopSignUpState> = _state

    private fun resetFormState() {
        _state.value = _state.value?.copy(isFailed = false, formErrorMsg = "")
    }

    private fun resetInputField() {
        _state.value = _state.value?.copy(
            email = "",
            isEmailError = false,
            emailErrorMsg = "",
            password = "",
            isPasswordError = false,
            passwordErrorMsg = "",
            repairShopName = "",
            isRepairShopNameError = false,
            repairShopNameErrorMsg = ""
        )
    }

    fun onChangeEmailField(value: String) {
        if (_state.value?.isFailed!!) resetFormState()
        _state.value = _state.value?.copy(email = value)
    }

    fun onChangePasswordField(value: String) {
        if (_state.value?.isFailed!!) resetFormState()
        _state.value = _state.value?.copy(password = value)
    }

    fun onChangeRepairShopNameField(value: String) {
        if (_state.value?.isFailed!!) resetFormState()
        _state.value = _state.value?.copy(repairShopName = value)
    }

    fun signUpHandler(onSuccess: () -> Unit) {
        try {
            _state.value = _state.value?.copy(isLoading = true)
            // Repair shop name validation
            inputFieldValidation(_state.value?.repairShopName!!, callbackWhenEmpty = {
                _state.value = _state.value?.copy(
                    isRepairShopNameError = true,
                    repairShopNameErrorMsg = InputFieldError.REQUIRED
                )
            })

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

            if (!_state.value?.isEmailError!! && !_state.value?.isPasswordError!! && !_state.value?.isRepairShopNameError!!) {
                // SIgn up process
                auth.createUserWithEmailAndPassword(_state.value?.email!!, _state.value?.password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            if (auth.currentUser != null) {
                                val user = UserData(
                                    name = _state.value?.repairShopName!!,
                                    role = ROLE.REPAIR_SHOP_OWNER
                                )
                                var schedules = mutableListOf<Schedule>()
                                days.forEach {
                                    schedules.add(Schedule(it))
                                }
                                val repairShopData = RepairShop(
                                    name = _state.value?.repairShopName!!,
                                    photo = "default.jpg",
                                    schedule = schedules
                                )
                                val usersRef =
                                    db.collection("users").document(auth.currentUser!!.uid)
                                val repairShopsRef =
                                    db.collection("repairShops").document(auth.currentUser!!.uid)

                                db.runBatch {
                                    // Create users in firestore database
                                    usersRef.set(user)

                                    // Create repairShops in firestore data
                                    repairShopsRef.set(repairShopData)
                                }.addOnFailureListener {
                                    _state.value = _state.value?.copy(
                                        isFailed = true,
                                        formErrorMsg = SignUpError.FAILED
                                    )
                                    auth.currentUser!!.delete()
                                }

                            }
                            resetInputField()
                            onSuccess()
                        } else {
                            // If sign in fails, display a message to the user.
                            val formErrorMsg =
                                if (task.exception is FirebaseAuthException) {
                                    SignUpError.EMAIL_IN_USE
                                } else {
                                    SignUpError.DEFAULT
                                }
                            _state.value = _state.value?.copy(isFailed = true, formErrorMsg = formErrorMsg)
                        }
                        _state.value = _state.value?.copy(isLoading = false)
                    }
            } else {
                _state.value = _state.value?.copy(isLoading = false)
            }
        } catch (e: Exception) {
            Log.d("Repair Shop Sign Up", e.message.toString())
        }
    }
}