package com.example.befine.screens.client.home

import androidx.lifecycle.ViewModel
import com.example.befine.model.RepairShop
import com.example.befine.repository.RepairShopRepository

class HomeViewModel(private val repository: RepairShopRepository): ViewModel() {
    fun getAllRepairShops(): List<RepairShop> {
        return repository.getRepairShops()
    }
}