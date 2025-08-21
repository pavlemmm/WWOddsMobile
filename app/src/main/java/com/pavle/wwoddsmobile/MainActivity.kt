package com.pavle.wwoddsmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pavle.wwoddsmobile.data.auth.Session
import com.pavle.wwoddsmobile.ui.screens.HomeScreen
import com.pavle.wwoddsmobile.ui.screens.LoginScreen
import com.pavle.wwoddsmobile.ui.screens.RegisterScreen
import com.pavle.wwoddsmobile.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppContent()
            }
        }
    }
}

@Composable
private fun AppContent() {
    val nav = rememberNavController()
    MaterialTheme {
        Surface {
            NavHost(navController = nav, startDestination = "login") {
                composable("login") {
                    LoginScreen(
                        onSignInSuccess = {
                            nav.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        },
                        onGoToRegister = { nav.navigate("register") }
                    )
                }
                composable("register") {
                    RegisterScreen(
                        onBack = { nav.popBackStack() },
                        onAccountCreated = {
                            nav.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }
                composable("home") {
                    HomeScreen(
                        onLogout = {
                            Session.clear()
                            nav.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}
