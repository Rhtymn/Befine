package com.example.befine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.befine.navigation.Screen
import com.example.befine.screens.LoginScreen
import com.example.befine.screens.RepairShopSignUpScreen
import com.example.befine.screens.SignUpScreen
import com.example.befine.screens.chat.channel.ChannelScreen
import com.example.befine.screens.client.HomeScreen
import com.example.befine.screens.profile.ProfileScreen
import com.example.befine.screens.repairshopowners.RepairShopHome
import com.example.befine.ui.theme.BefineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BefineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    BefineApp()
                }
            }
        }
    }
}

@Composable
fun BefineApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    fun goToLogin(): () -> Unit = {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }

    fun goToRegularHomeScreen() = { -> navController.navigate(Screen.Home.route) }
    fun goToRepairShopHome() = { -> navController.navigate(Screen.RepairShopHome.route) }
    fun goToRegisterUser() = { ->
        navController.navigate(Screen.RegisterUser.route)
    }

    val goToProfile: (role: String) -> Unit = { role ->
        navController.navigate(Screen.Profile.createRoute(role))
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
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
                navigateToProfile = { goToProfile("REGULAR_USER") },
                navigateToChatChannel = { navController.navigate(Screen.ChatChannel.route) }
            )
        }
        composable(
            Screen.Profile.route,
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) {
            val role = it.arguments?.getString("role")
            ProfileScreen(navigateToLogin = goToLogin(), role = role.toString())
        }
        composable(Screen.ChatChannel.route) {
            ChannelScreen()
        }
        composable(Screen.RepairShopHome.route) {
            RepairShopHome(navigateToProfile = { goToProfile("REPAIR_SHOP_OWNER") })
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BefineTheme {
        BefineApp()
    }
}