package com.example.befine.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.befine.R
import com.example.befine.components.ui.TopBar
import com.example.befine.components.ui.profile.ActionButton
import com.example.befine.components.ui.profile.ProfileInformation
import com.example.befine.components.ui.profile.actionButtonIconModifier
import com.example.befine.firebase.Auth
import com.example.befine.model.AuthData
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.ROLE
import com.example.befine.utils.Screen
import com.example.befine.utils.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    role: String = ROLE.CLIENT,
    navigateToLogin: () -> Unit,
    navigateToEditRepairShop: (userId: String) -> Unit = {},
    navController: NavHostController,
    auth: FirebaseAuth = Auth.getInstance().getAuth(),
    profileViewModel: ProfileViewModel = viewModel(factory = ViewModelFactory())
) {
    val context = LocalContext.current

    val state: AuthData by profileViewModel.state.observeAsState(AuthData())
    LaunchedEffect(true) {
        CoroutineScope(Dispatchers.IO).launch {
            profileViewModel.getUserPreference(context = context)
        }
    }

    val logoutHandler = {
        navigateToLogin()
        auth.signOut()
        CoroutineScope(Dispatchers.IO).launch {
            profileViewModel.deleteUserPreference(context)
        }
    }
    Scaffold(
        topBar = {
            TopBar(title = "Profile") {
                navController.popBackStack()
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(
                    innerPadding
                )
                .padding(vertical = Screen.paddingVertical)
        ) {
            ProfileInformation(
                name = state.name ?: "",
                email = state.email ?: "",
                role = role,
                photo = state.photo.toString()
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            if (role == ROLE.REPAIR_SHOP_OWNER) {
                ActionButton(
                    onClick = { navigateToEditRepairShop(auth.currentUser?.uid!!) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "",
                            modifier = actionButtonIconModifier
                        )
                    },
                    text = "Repair Shop Information"
                )
            }
            ActionButton(
                onClick = { logoutHandler() },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_logout_24),
                        contentDescription = "",
                        modifier = actionButtonIconModifier
                    )
                },
                text = "Logout"
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    BefineTheme {
        ProfileScreen(
            navigateToLogin = { /*TODO*/ }, navController = rememberNavController(
            )
        )
    }
}