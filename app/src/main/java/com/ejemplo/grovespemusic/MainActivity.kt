package com.ejemplo.grovespemusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ejemplo.grovespemusic.model.User
import com.ejemplo.grovespemusic.model.UserRole
import com.ejemplo.grovespemusic.ui.screens.*
import com.ejemplo.grovespemusic.ui.theme.GrovespeMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrovespeMusicTheme {
                // Estado global para simular el usuario y su rol
                var currentUser by remember { 
                    mutableStateOf(User("u1", "Explorador CDMX", "user@groovespace.com", UserRole.FAN)) 
                }
                
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("catalog") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onRegisterNavigate = {
                                navController.navigate("register")
                            }
                        )
                    }
                    
                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = {
                                navController.navigate("catalog") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onBackToLogin = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("catalog") {
                        CatalogScreen(
                            onBandClick = { bandId ->
                                navController.navigate("band_detail/$bandId")
                            },
                            onProfileClick = {
                                navController.navigate("profile")
                            }
                        )
                    }

                    composable(
                        route = "band_detail/{bandId}",
                        arguments = listOf(navArgument("bandId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val bandId = backStackEntry.arguments?.getString("bandId") ?: ""
                        BandDetailScreen(
                            bandId = bandId,
                            currentUser = currentUser,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("profile") {
                        ProfileScreen(
                            user = currentUser,
                            onBack = { navController.popBackStack() },
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("catalog") { inclusive = true }
                                }
                            },
                            onRoleToggle = {
                                val newRole = if (currentUser.role == UserRole.FAN) UserRole.ARTIST else UserRole.FAN
                                currentUser = currentUser.copy(role = newRole)
                            }
                        )
                    }
                }
            }
        }
    }
}
