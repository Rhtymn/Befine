package com.example.befine.screens.client.home

import androidx.lifecycle.ViewModel
import com.example.befine.model.RepairShop
import com.example.befine.model.RepairShopWithId
import com.example.befine.repository.RepairShopRepository

class HomeViewModel(private val repository: RepairShopRepository) : ViewModel() {
    fun getAllRepairShop(): List<RepairShopWithId> {
        val datas = repository.getRepairShops()
        val listOfRepairShop = mutableListOf<RepairShopWithId>()
        datas.forEach { entry ->
            listOfRepairShop.add(RepairShopWithId(id = entry.key, repairShop = entry.value))
        }
        return listOfRepairShop
    }
}