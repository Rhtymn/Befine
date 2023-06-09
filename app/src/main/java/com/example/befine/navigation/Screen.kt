package com.example.befine.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object RepairShopHome : Screen("repairShopHome")
    object Profile : Screen("profile/{role}") {
        fun createRoute(role: String) = "profile/$role"
    }
    object Details : Screen("details")
    object EditRepairShop: Screen("editRepairShop")
    object ChatChannel : Screen("chatChannel")
    object Login : Screen("login")
    object RegisterUser : Screen("registerUser")
    object RegisterRepairShop : Screen("registerRepairShop")
}