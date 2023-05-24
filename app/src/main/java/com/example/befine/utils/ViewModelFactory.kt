package com.example.befine.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.befine.repository.RepairShopRepository
import com.example.befine.screens.chat.channel.ChannelViewModel
import com.example.befine.screens.client.home.HomeViewModel
import com.example.befine.screens.profile.ProfileViewModel

class ViewModelFactory(private val repository: RepairShopRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel() as T
        } else if (modelClass.isAssignableFrom(ChannelViewModel::class.java)) {
            return ChannelViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
    }
}