package com.example.befine.data

import com.example.befine.model.RepairShop
import com.example.befine.utils.STATUS

object RepairShopData {
    val repairShop = listOf(
        RepairShop(
            1,
            "Bengkel Amanah",
            address = "Jl Indonesia Merdeka No 17",
            phoneNumber = "085222221111",
            latitude = "222222",
            longitude = "222222",
            status = STATUS.OPEN
        ),
        RepairShop(
            2,
            "Bengkel Jaya",
            address = "Jl Bandung Lautan Api No 11",
            phoneNumber = "085222221111",
            latitude = "222222",
            longitude = "222222",
            status = STATUS.CLOSED
        ),
        RepairShop(
            3,
            "Bengkel Naratama",
            address = "Jl Gandhi Baru No 20",
            phoneNumber = "085222221111",
            latitude = "222222",
            longitude = "222222",
            status = STATUS.OPEN
        ),
        RepairShop(
            4,
            "Bengkel Subakti",
            address = "Jl Sara Maraya No 13",
            phoneNumber = "085222221111",
            latitude = "222222",
            longitude = "222222",
            status = STATUS.CLOSED
        ),
        RepairShop(
            5,
            "Bengkel Baraya",
            address = "Jl Nara Malina No 06",
            phoneNumber = "085222221111",
            latitude = "222222",
            longitude = "222222",
            status = STATUS.OPEN
        ),
    )
}