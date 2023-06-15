package com.example.befine.screens.client.details

import android.net.Uri
import com.example.befine.model.RepairShop
import com.google.android.gms.maps.model.LatLng

data class RepairShopDetailsState(
    val location: LatLng? = null,
    val repairShop: RepairShop? = null,
    val imageUri: Uri? = Uri.EMPTY,
)