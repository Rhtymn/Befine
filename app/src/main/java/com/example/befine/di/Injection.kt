package com.example.befine.di

import com.example.befine.repository.RepairShopRepository

object Injection {
    fun provideRepairShopRepository(): RepairShopRepository {
        return RepairShopRepository.getInstance()
    }
}