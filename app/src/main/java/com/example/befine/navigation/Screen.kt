package com.example.befine.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object RepairShopHome : Screen("repairShopHome")
    object Profile : Screen("profile/{role}") {
        fun createRoute(role: String) = "profile/$role"
    }
    object Details : Screen("details/{repairShopId}") {
        fun createRoute(repairShopId: String) = "details/$repairShopId"
    }
    object EditRepairShop: Screen("editRepairShop/{userId}") {
        fun createRoute(userId: String) = "editRepairShop/$userId"
    }
    object ChatChannel : Screen("chatChannel")

    object ChatRoom: Screen("chatRoom/{receiver}") {
        fun createRoute(receiver: String) = "chatRoom/$receiver"
    }
    object Login : Screen("login")
    object RegisterUser : Screen("registerUser")
    object RegisterRepairShop : Screen("registerRepairShop")
}