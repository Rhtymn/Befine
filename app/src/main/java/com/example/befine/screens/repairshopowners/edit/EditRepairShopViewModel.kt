package com.example.befine.screens.repairshopowners.edit

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.model.RepairShop
import com.example.befine.model.Schedule
import com.example.befine.utils.InputFieldError
import com.example.befine.utils.days
import com.example.befine.utils.inputFieldValidation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File

class EditRepairShopViewModel : ViewModel() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage: FirebaseStorage = Firebase.storage
    private val _fieldState = MutableLiveData(EditRepairShopFieldState())
    val fieldState: LiveData<EditRepairShopFieldState> = _fieldState

    private val _locationFieldState = MutableLiveData(LocationFieldState())
    val locationFieldState: LiveData<LocationFieldState> = _locationFieldState

    private val _schedulesState = MutableLiveData(SchedulesState())
    val schedulesState: LiveData<SchedulesState> = _schedulesState

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isUserUploadFile = MutableLiveData(false)

    private val _capturedImageUri = MutableLiveData(Uri.EMPTY)
    val capturedImageUri: LiveData<Uri> = _capturedImageUri

    fun resetInputField() {
        _fieldState.value = _fieldState.value?.copy(
            isRepairShopNameError = false,
            repairShopNameErrorMsg = "",
            isAddressError = false,
            addressErrorMsg = "",
            isDescriptionError = false,
            descriptionErrorMsg = "",
            isPhoneNumberError = false,
            phoneNumberErrorMsg = "",
        )
        _isUserUploadFile.value = false
    }

    fun setLocation(location: LocationFieldState) {
        _locationFieldState.value = _locationFieldState.value?.copy(
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    fun setUserIsUploadFile(value: Boolean) {
        _isUserUploadFile.value = value
    }

    fun setCapturedImageUri(value: Uri) {
        _capturedImageUri.value = value
    }

    fun onChangeRepairShopName(value: String) {
        _fieldState.value = _fieldState.value?.copy(repairShopName = value)
    }

    fun onChangeAddress(value: String) {
        _fieldState.value = _fieldState.value?.copy(address = value)
    }

    fun onChangeDescription(value: String) {
        _fieldState.value = _fieldState.value?.copy(description = value)
    }

    fun onChangePhoneNumber(value: String) {
        _fieldState.value = _fieldState.value?.copy(phoneNumber = value)
    }

    fun setStartWeekdayHours(value: String) {
        _schedulesState.value = _schedulesState.value?.copy(startWeekdayHours = value)
    }

    fun setEndWeekdayHours(value: String) {
        _schedulesState.value = _schedulesState.value?.copy(endWeekdayHours = value)
    }

    fun setStartWeekendHours(value: String) {
        _schedulesState.value = _schedulesState.value?.copy(startWeekendHours = value)
    }

    fun setEndWeekendHours(value: String) {
        _schedulesState.value = _schedulesState.value?.copy(endWeekendHours = value)
    }

    fun toggleSelectedDay(idx: Int) {
        val oldSelectedDay = _schedulesState.value?.selectedDay
        val newSelectedDay = mutableMapOf<String, Boolean>()
        for (i in 0..6) {
            if (i == idx) {
                newSelectedDay[days[i]] = !oldSelectedDay?.get(days[i])!!
            } else {
                newSelectedDay[days[i]] = oldSelectedDay?.get(days[i])!!
            }
        }

        _schedulesState.value = _schedulesState.value?.copy(selectedDay = newSelectedDay)
    }

    suspend fun getInitialData(userId: String) {
        val repairShop: RepairShop =
            db.collection("repairShops").document(userId)
                .get().await().toObject<RepairShop>() ?: RepairShop()

        val repairShopName = if (repairShop.name.isNullOrBlank()) "" else repairShop.name.toString()
        val address = if (repairShop.address.isNullOrBlank()) "" else repairShop.address.toString()
        val description =
            if (repairShop.description.isNullOrBlank()) "" else repairShop.description.toString()
        val phoneNumber =
            if (repairShop.phone_number.isNullOrBlank()) "" else repairShop.phone_number.toString()
        val latitude =
            if (repairShop.latitude.isNullOrBlank()) 0.0 else repairShop.latitude.toDouble()
        val longitude =
            if (repairShop.longitude.isNullOrBlank()) 0.0 else repairShop.longitude.toDouble()

        _fieldState.value = _fieldState.value?.copy(
            repairShopName = repairShopName,
            address = address,
            description = description,
            phoneNumber = phoneNumber
        )

        _locationFieldState.value = _locationFieldState.value?.copy(
            latitude = latitude,
            longitude = longitude
        )

        val selectedDay = mutableMapOf<String, Boolean>()
        for (i in 0..6) {
            selectedDay[days[i]] =
                if (repairShop.schedule?.get(i)?.status.isNullOrBlank()) false else repairShop.schedule?.get(
                    i
                )?.status == "open"

            if (i < 5) { // weekdays
                if (!repairShop.schedule?.get(i)?.operationalHours.isNullOrBlank()) {
                    val startWeekdayHours =
                        repairShop.schedule?.get(i)?.operationalHours?.slice(0..4) ?: "00:00"
                    val endWeekdaysHours =
                        repairShop.schedule?.get(i)?.operationalHours?.slice(6..10) ?: "00:00"

                    _schedulesState.value = _schedulesState.value?.copy(
                        startWeekdayHours = startWeekdayHours,
                        endWeekdayHours = endWeekdaysHours
                    )
                }
            } else {
                if (!repairShop.schedule?.get(i)?.operationalHours.isNullOrBlank()) {
                    val startWeekendHours =
                        repairShop.schedule?.get(i)?.operationalHours?.slice(0..4) ?: "00:00"
                    val endWeekendHours =
                        repairShop.schedule?.get(i)?.operationalHours?.slice(6..10) ?: "00:00"

                    _schedulesState.value = _schedulesState.value?.copy(
                        startWeekendHours = startWeekendHours, endWeekdayHours = endWeekendHours
                    )
                }
            }
        }
        _schedulesState.value = _schedulesState.value?.copy(selectedDay = selectedDay)

        if (!repairShop.photo.isNullOrBlank()) {
            val imageRef = storage.reference.child("images/${repairShop.photo}")
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                _capturedImageUri.value = uri
            }
        }
    }

    fun updateDataHandler(file: File, userId: String, context: Context) {
        try {
            _isLoading.value = true

            // repair shop name validation
            inputFieldValidation(_fieldState.value?.repairShopName!!) {
                _fieldState.value = _fieldState.value?.copy(
                    isRepairShopNameError = true,
                    repairShopNameErrorMsg = InputFieldError.REQUIRED
                )
            }

            // address validation
            inputFieldValidation(_fieldState.value?.address!!) {
                _fieldState.value = _fieldState.value?.copy(
                    isAddressError = true,
                    addressErrorMsg = InputFieldError.REQUIRED
                )
            }

            // description validation
            inputFieldValidation(_fieldState.value?.description!!) {
                _fieldState.value = _fieldState.value?.copy(
                    isDescriptionError = true,
                    descriptionErrorMsg = InputFieldError.REQUIRED
                )
            }

            // phone number validation
            inputFieldValidation(_fieldState.value?.phoneNumber!!) {
                _fieldState.value = _fieldState.value?.copy(
                    isPhoneNumberError = true,
                    phoneNumberErrorMsg = InputFieldError.REQUIRED
                )
            }

            if (_fieldState.value?.phoneNumber?.length!! < 10) {
                _fieldState.value = _fieldState.value?.copy(
                    isPhoneNumberError = true,
                    phoneNumberErrorMsg = "Minimum 10 characters"
                )
            } else if (_fieldState.value?.phoneNumber?.length!! > 15) {
                _fieldState.value = _fieldState.value?.copy(
                    isPhoneNumberError = true,
                    phoneNumber = "Maximum 15 characters"
                )
            }

            // Checking error availability
            if (!_fieldState.value?.isRepairShopNameError!! && !_fieldState.value?.isAddressError!! && !_fieldState.value?.isDescriptionError!! && !_fieldState.value?.isPhoneNumberError!!) {

                val newSchedules = mutableListOf<Schedule>()
                for (i in 0..6) {
                    val day = days[i]
                    val status =
                        if (_schedulesState.value?.selectedDay?.get(days[i]) == true) "open" else "closed"

                    if (i < 5) { // weekdays
                        val operationalHours =
                            if (status == "open") "${_schedulesState.value?.startWeekdayHours}—${_schedulesState.value?.endWeekdayHours}" else null

                        newSchedules.add(
                            Schedule(
                                day = day,
                                status = status,
                                operationalHours = operationalHours
                            )
                        )

                    } else { // weekend
                        val operationalHours =
                            if (status == "open") "${_schedulesState.value?.startWeekendHours}—${_schedulesState.value?.endWeekendHours}" else null

                        newSchedules.add(
                            Schedule(
                                day = day,
                                status = status,
                                operationalHours = operationalHours
                            )
                        )

                    }
                }
                val photo =
                    if (_isUserUploadFile.value!!) "$userId.${file.extension}" else "default.jpg"

                val newRepairShopData = RepairShop(
                    name = _fieldState.value!!.repairShopName,
                    address = _fieldState.value!!.address,
                    description = _fieldState.value!!.description,
                    latitude = _locationFieldState.value?.latitude.toString(),
                    longitude = _locationFieldState.value?.longitude.toString(),
                    phone_number = _fieldState.value!!.phoneNumber,
                    photo = photo,
                    schedule = newSchedules
                )

                // upload file process
                if (_isUserUploadFile.value!!) {
                    Log.d("EDIT_VIEW_MODEL", file.toString())
                    val storageRef = storage.reference
                    val userRef =
                        storageRef.child("images/$userId.${file.extension}")

                    userRef.putFile(_capturedImageUri.value!!).addOnSuccessListener {
                        // update process
                        db.collection("repairShops").document(userId)
                            .set(newRepairShopData).addOnSuccessListener {
                                _isLoading.value = false
                                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                                userRef.delete()
                            }
                    }
                } else {
                    // update process
                    db.collection("repairShops").document(userId)
                        .set(newRepairShopData).addOnSuccessListener {
                            _isLoading.value = false
                            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                }
                _isUserUploadFile.value = false
            } else {
                _isLoading.value = false
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}