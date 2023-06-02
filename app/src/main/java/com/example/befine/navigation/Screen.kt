package com.example.befine.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Details : Screen("details")
    object ChatChannel : Screen("chatChannel")
}