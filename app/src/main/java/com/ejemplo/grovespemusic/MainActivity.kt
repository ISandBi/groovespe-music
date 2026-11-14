package com.ejemplo.grovespemusic

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ejemplo.grovespemusic.model.Song
import com.ejemplo.grovespemusic.model.User
import com.ejemplo.grovespemusic.model.UserRole
import com.ejemplo.grovespemusic.ui.screens.*
import com.ejemplo.grovespemusic.ui.theme.GrovespeMusicTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrovespeMusicTheme {
                // Estado global para simular el usuario y su rol
                var currentUser by remember { 
                    mutableStateOf(User("u1", "Explorador CDMX", "user@groovespace.com", UserRole.FAN)) 
                }
                
                var currentSong by remember { mutableStateOf<Song?>(null) }
                var isPlaying by remember { mutableStateOf(false) }
                var progress by remember { mutableStateOf(0f) }
                var showFullPlayer by remember { mutableStateOf(false) }

                val context = this
                
                // Manejo de reproducción real
                LaunchedEffect(currentSong) {
                    if (currentSong != null) {
                        try {
                            mediaPlayer?.stop()
                            mediaPlayer?.release()
                            
                            // Buscamos el audio. Intentamos varios nombres según lo que has configurado.
                            val resId = context.resources.getIdentifier("ic_launcher_round", "mipmap", context.packageName).let {
                                if (it == 0) context.resources.getIdentifier("audio_prueba", "raw", context.packageName) else it
                            }

                            if (resId != 0) {
                                mediaPlayer = MediaPlayer.create(context, resId).apply {
                                    isLooping = true
                                    if (isPlaying) start()
                                }
                            }
                        } catch (e: Exception) {
                            mediaPlayer = null
                        }
                    }
                }

                LaunchedEffect(isPlaying) {
                    try {
                        if (isPlaying) mediaPlayer?.start() else mediaPlayer?.pause()
                    } catch (e: Exception) { }
                }

                // Simulación de progreso
                LaunchedEffect(isPlaying, currentSong) {
                    while (isPlaying && currentSong != null) {
                        delay(1000)
                        val currentPos = progress + 0.01f
                        progress = if (currentPos > 1f) 0f else currentPos
                    }
                }

                val navController = rememberNavController()

                Box {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("catalog") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onRegisterNavigate = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegisterSuccess = { navController.navigate("catalog") },
                                onBackToLogin = { navController.popBackStack() }
                            )
                        }
                        composable("catalog") {
                            CatalogScreen(
                                onBandClick = { bandId -> navController.navigate("band_detail/$bandId") },
                                onProfileClick = { navController.navigate("profile") },
                                currentSong = currentSong,
                                isPlaying = isPlaying,
                                onTogglePlay = { isPlaying = !isPlaying },
                                onOpenPlayer = { showFullPlayer = true }
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
                                currentTrackId = currentSong?.id ?: "",
                                isPlayingGlobal = isPlaying,
                                onBack = { navController.popBackStack() },
                                onPlaySong = { song ->
                                    currentSong = song
                                    isPlaying = true
                                    progress = 0f
                                }
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
                                    currentUser = currentUser.copy(role = if (currentUser.role == UserRole.FAN) UserRole.ARTIST else UserRole.FAN)
                                }
                            )
                        }
                    }

                    // Capa del reproductor completo
                    if (showFullPlayer && currentSong != null) {
                        FullPlayerScreen(
                            song = currentSong!!,
                            isPlaying = isPlaying,
                            progress = progress,
                            isPremium = currentUser.role == UserRole.ARTIST,
                            onClose = { showFullPlayer = false },
                            onTogglePlay = { isPlaying = !isPlaying },
                            onPrevious = { },
                            onNext = { },
                            onSeek = { progress = it }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
