package com.example.befine.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.befine.R
import com.example.befine.components.ui.profile.ActionButton
import com.example.befine.components.ui.profile.ProfileInformation
import com.example.befine.components.ui.profile.actionButtonIconModifier
import com.example.befine.firebase.Auth
import com.example.befine.ui.theme.BefineTheme
import com.example.befine.utils.ROLE
import com.example.befine.utils.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    role: String = ROLE.CLIENT,
    navigateToLogin: () -> Unit,
    auth: FirebaseAuth = Auth.getInstance().getAuth()
) {
    val logoutHandler = {
        auth.signOut()
        navigateToLogin()
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.padding(
                horizontal = Screen.paddingHorizontal,
                vertical = Screen.paddingVertical
            )
        ) {
            ProfileInformation(name = "Andy Machisa", email = "machisa@gmail.com")
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            if (role == "REPAIR_SHOP_OWNER") {
                ActionButton(
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