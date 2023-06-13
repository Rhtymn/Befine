package com.example.befine.di

import com.example.befine.repository.RepairShopRepository
import com.example.befine.repository.UsersRepository

object Injection {
    fun provideRepairShopRepository(): RepairShopRepository {
        return RepairShopRepository.getInstance()
    }

    fun provideUsersRepository(): UsersRepository {
        return UsersRepository.getInstance()
    }
}