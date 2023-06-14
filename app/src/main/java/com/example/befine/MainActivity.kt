package com.example.befine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.befine.screens.chat.room.ChatRoomState
import com.example.befine.navigation.Screen
import com.example.befine.screens.login.LoginScreen
import com.example.befine.screens.register.repairshop.RepairShopSignUpScreen
import com.example.befine.screens.register.regular.SignUpScreen
import com.example.befine.screens.chat.channel.ChannelScreen
import com.example.befine.screens.chat.room.ChatRoom
import com.example.befine.screens.client.home.HomeScreen
import com.example.befine.screens.client.details.RepairShopDetailsScreen
import com.example.befine.screens.profile.ProfileScreen
import com.example.befine.screens.repairshopowners.EditRepairShopScreen
import com.example.befine.screens.repairshopowners.home.RepairShopHome
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.ROLE
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BefineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().background(color = Color.White),
                ) {
                    BefineApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BefineApp(
    navController: NavHostController = rememberNavController(),
) {
    fun goToLogin(): () -> Unit = {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }

    fun goToRegularHomeScreen(): () -> Unit = { navController.navigate(Screen.Home.route) }
    fun goToRepairShopHome(): () -> Unit = { navController.navigate(Screen.RepairShopHome.route) }
    fun goToRegisterUser(): () -> Unit = {
        navController.navigate(Screen.RegisterUser.route)
    }

    val goToEditRepairShopScreen: (userId: String) -> Unit =
        { userId -> navController.navigate(Screen.EditRepairShop.createRoute(userId)) }
    val goToRepairShopDetailScreen: (repairShopId: String) -> Unit =
        { repairShopId -> navController.navigate(Screen.Details.createRoute(repairShopId)) }
    val goToProfile: (role: String) -> Unit = { role ->
        navController.navigate(Screen.Profile.createRoute(role))
    }
    val goToChatRoom: (chatRoomState: ChatRoomState) -> Unit = { chatRoomState ->
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(ChatRoomState::class.java).lenient()
        val chatRoomStateJson = jsonAdapter.toJson(chatRoomState)

        navController.navigate(Screen.ChatRoom.createRoute(chatRoomStateJson))
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                navigateToUserRegister = goToRegisterUser(),
                navigateToRegularHome = goToRegularHomeScreen(),
                navigateToRepairShopHome = goToRepairShopHome()
            )
        }
        composable(Screen.RegisterUser.route) {
            SignUpScreen(
                navigateToLogin = goToLogin(),
                navigateToRepairShopRegister = { navController.navigate(Screen.RegisterRepairShop.route) }
            )
        }
        composable(Screen.RegisterRepairShop.route) {
            RepairShopSignUpScreen(
                navigateToLogin = goToLogin(),
                navigateToUserRegister = goToRegisterUser()
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                navigateToProfile = { goToProfile(ROLE.CLIENT) },
                navigateToChatChannel = { navController.navigate(Screen.ChatChannel.route) },
                navigateToRepairShopDetail = goToRepairShopDetailScreen
            )
        }
        composable(
            Screen.Profile.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) {
            val role = it.arguments?.getString("role")
            ProfileScreen(
                navigateToLogin = goToLogin(),
                role = role.toString(),
                navController = navController,
                navigateToEditRepairShop = goToEditRepairShopScreen,
            )
        }
        composable(Screen.ChatChannel.route) {
            ChannelScreen(navController = navController)
        }
        composable(Screen.RepairShopHome.route) {
            RepairShopHome(navigateToProfile = { goToProfile(ROLE.REPAIR_SHOP_OWNER) })
        }
        composable(
            Screen.EditRepairShop.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            val userId = it.arguments?.getString("userId")
            EditRepairShopScreen(
                userId = userId.toString(),
                navController = navController
            )
        }
        composable(
            Screen.Details.route,
            arguments = listOf(navArgument("repairShopId") { type = NavType.StringType })
        ) {
            val repairShopId = it.arguments?.getString("repairShopId")
            RepairShopDetailsScreen(
                repairShopId = repairShopId.toString(),
                navController = navController,
                navigateToChatRoom = goToChatRoom
            )
        }
        composable(
            Screen.ChatRoom.route,
            arguments = listOf(navArgument("receiver") { type = NavType.StringType })
        ) {
            val chatRoomStateJson = it.arguments?.getString("receiver")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(ChatRoomState::class.java).lenient()
            val chatRoomState = jsonAdapter.fromJson(chatRoomStateJson!!)

            ChatRoom(chatRoomState!!, navController = navController)
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BefineTheme {
        BefineApp()
    }
}