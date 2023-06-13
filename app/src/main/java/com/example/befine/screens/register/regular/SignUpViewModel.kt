package com.example.befine.screens.register.regular

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.firebase.Auth
import com.example.befine.model.UserData
import com.example.befine.repository.UsersRepository
import com.example.befine.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class SignUpViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    private val auth: FirebaseAuth = Auth.getInstance().getAuth()
    private val _state = MutableLiveData(SignUpState())
    val state: LiveData<SignUpState> = _state

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
            fullName = "",
            isFullNameError = false,
            fullNameErrorMsg = ""
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

    fun onChangeFullNameField(value: String) {
        if (_state.value?.isFailed!!) resetFormState()
        _state.value = _state.value?.copy(fullName = value)
    }

    private fun resetErrorFieldState() {
        _state.value = _state.value?.copy(
            isEmailError = false,
            isPasswordError = false,
            isFullNameError = false
        )
    }

    fun signUpHandler(onSuccess: () -> Unit) {
        try {
            resetErrorFieldState()
            _state.value = _state.value?.copy(isLoading = true)

            // Fullname validation
            inputFieldValidation(_state.value?.fullName!!, callbackWhenEmpty = {
                _state.value = _state.value?.copy(
                    isFullNameError = true,
                    fullNameErrorMsg = InputFieldError.REQUIRED
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

            // Checking error availability
            if (!_state.value?.isEmailError!! && !_state.value?.isPasswordError!! && !_state.value?.isFullNameError!!) {
                // Sign up process
                auth.createUserWithEmailAndPassword(_state.value?.email!!, _state.value?.password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            if (auth.currentUser != null) {
                                val user = UserData(
                                    name = _state.value?.fullName!!,
                                    role = ROLE.CLIENT
                                )

                                // Create users in firestore database
                                usersRepository.createUser(auth.currentUser!!.uid, user) {
                                    // Callback when failed
                                    auth.currentUser!!.delete()
                                    _state.value = _state.value?.copy(
                                        isFailed = true,
                                        formErrorMsg = SignUpError.FAILED
                                    )
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
                            _state.value =
                                _state.value?.copy(isFailed = true, formErrorMsg = formErrorMsg)
                        }
                        _state.value = _state.value?.copy(isLoading = false)
                    }
            } else {
                _state.value = _state.value?.copy(isLoading = false)
            }
        } catch (e: Exception) {
            Log.d("Sign Up Screen", e.message.toString())
        }
    }
}