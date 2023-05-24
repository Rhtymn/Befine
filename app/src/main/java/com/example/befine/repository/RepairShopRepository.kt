package com.example.befine.repository

import com.example.befine.data.RepairShopData
import com.example.befine.model.RepairShop

class RepairShopRepository {
    fun getRepairShops(): List<RepairShop> {
        return RepairShopData.repairShop
    }

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
}