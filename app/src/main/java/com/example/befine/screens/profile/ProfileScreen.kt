package com.example.befine.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.befine.R
import com.example.befine.components.ui.TopBar
import com.example.befine.components.ui.profile.ActionButton
import com.example.befine.components.ui.profile.ProfileInformation
import com.example.befine.components.ui.profile.actionButtonIconModifier
import com.example.befine.firebase.Auth
import com.example.befine.model.User
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.ROLE
import com.example.befine.utils.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen(
    role: String = ROLE.CLIENT,
    navigateToLogin: () -> Unit,
    navigateToEditRepairShop: () -> Unit = {},
    navigateToRegularUserHome: () -> Unit = {},
    navigateToRepairShopHome: () -> Unit = {},
    auth: FirebaseAuth = Auth.getInstance().getAuth(),
    db: FirebaseFirestore = Firebase.firestore
) {
    var username by remember { mutableStateOf("") }
    val email = if (auth.currentUser != null) auth.currentUser!!.email.toString() else ""

    LaunchedEffect(true) {
        val user: User? =
            db.collection("users").document(auth.currentUser!!.uid).get().await().toObject<User>()
        if (user != null) {
            username = user.name.toString()
        }
    }

    val logoutHandler = {
        navigateToLogin()
        auth.signOut()
    }
    Scaffold(
        topBar = {
            TopBar(title = "Profile") {
                if (role == ROLE.CLIENT) {
                    navigateToRegularUserHome()
                } else if (role == ROLE.REPAIR_SHOP_OWNER) {
                    navigateToRepairShopHome()
                }
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(
                    innerPadding
                )
                .padding(horizontal = Screen.paddingHorizontal, vertical = Screen.paddingVertical)
        ) {
            ProfileInformation(name = username, email = email)
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            if (role == ROLE.REPAIR_SHOP_OWNER) {
                ActionButton(
                    onClick = navigateToEditRepairShop,
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
                onClick = logoutHandler,
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
        ProfileScreen(navigateToLogin = { /*TODO*/ })
    }
}