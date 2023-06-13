package com.example.befine.screens.client.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.befine.model.RepairShop
import com.example.befine.model.RepairShopWithId
import com.example.befine.repository.RepairShopRepository

class HomeViewModel(private val repository: RepairShopRepository) : ViewModel() {
    private val _repairShopData = MutableLiveData(listOf<RepairShopWithId>())
    val repairShopData: LiveData<List<RepairShopWithId>> = _repairShopData

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getAllRepairShop()
    }

    fun getAllRepairShop() {
        _isLoading.value = true
        val datas = repository.getRepairShops()
        val listOfRepairShop = mutableListOf<RepairShopWithId>()
        datas.forEach { entry ->
            listOfRepairShop.add(RepairShopWithId(id = entry.key, repairShop = entry.value))
        }
        _repairShopData.value = listOfRepairShop
        _isLoading.value = false
    }
}