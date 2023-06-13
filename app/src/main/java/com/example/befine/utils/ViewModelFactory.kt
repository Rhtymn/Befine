package com.example.befine.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.befine.repository.RepairShopRepository
import com.example.befine.repository.UsersRepository
import com.example.befine.screens.chat.channel.ChannelViewModel
import com.example.befine.screens.client.home.HomeViewModel
import com.example.befine.screens.login.LoginViewModel
import com.example.befine.screens.profile.ProfileViewModel
import com.example.befine.screens.register.regular.SignUpViewModel
import com.example.befine.screens.register.repairshop.RepairShopSignUpViewModel

class ViewModelFactory(
    private val repository: RepairShopRepository? = null,
    private val usersRepository: UsersRepository? = null
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return repository?.let { HomeViewModel(it) } as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel() as T
        } else if (modelClass.isAssignableFrom(ChannelViewModel::class.java)) {
            return ChannelViewModel() as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel() as T
        } else if(modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return usersRepository?.let { SignUpViewModel(it) } as T
        } else if(modelClass.isAssignableFrom(RepairShopSignUpViewModel::class.java)) {
            return RepairShopSignUpViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class " + modelClass.name)
    }
}