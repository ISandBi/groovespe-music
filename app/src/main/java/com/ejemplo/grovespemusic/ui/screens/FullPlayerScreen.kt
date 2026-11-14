package com.ejemplo.grovespemusic.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ejemplo.grovespemusic.model.Song
import com.ejemplo.grovespemusic.ui.theme.*
import kotlin.random.Random

@Composable
fun FullPlayerScreen(
    song: Song,
    isPlaying: Boolean,
    progress: Float,
    isPremium: Boolean,
    onClose: () -> Unit,
    onTogglePlay: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
    ) {
        // Fondo Aurora Animado
        val infiniteTransition = rememberInfiniteTransition(label = "bg")
        val bgOffset by infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 1000f,
            animationSpec = infiniteRepeatable(tween(20000, easing = LinearEasing), RepeatMode.Reverse), label = "offset"
        )

        Canvas(modifier = Modifier.fillMaxSize().alpha(0.2f)) {
            drawRect(
                Brush.radialGradient(
                    colors = listOf(SoftPurple, Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(bgOffset, 0f),
                    radius = 2000f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = White, modifier = Modifier.size(36.dp))
                }
                Text("REPRODUCTOR CYBER", color = White, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                IconButton(onClick = { }) {
                    Icon(Icons.Default.MoreVert, null, tint = White)
                }
            }

            Spacer(modifier = Modifier.weight(0.4f))

            // Portada con Aura de Neón
            Box(contentAlignment = Alignment.Center) {
                Box(modifier = Modifier.size(300.dp).blur(50.dp).background(LimeGreen.copy(alpha = 0.15f), CircleShape))
                
                Surface(
                    modifier = Modifier.size(280.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(1.dp, Brush.linearGradient(listOf(LimeGreen, SoftPurple))),
                    shadowElevation = 30.dp
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1514525253361-bee8718a74a2?q=80&w=800",
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.3f))

            // Título y Artista
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(song.title, color = White, fontSize = 32.sp, fontWeight = FontWeight.Black, lineHeight = 36.sp)
                Text("CYBER CHILANGOS", color = LimeGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Progreso
            Column {
                Slider(
                    value = progress,
                    onValueChange = onSeek,
                    colors = SliderDefaults.colors(
                        thumbColor = LimeGreen,
                        activeTrackColor = LimeGreen,
                        inactiveTrackColor = White.copy(alpha = 0.1f)
                    )
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("0:00", color = GrayText, fontSize = 11.sp)
                    Text(song.duration, color = GrayText, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Controles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPrevious, modifier = Modifier.size(56.dp)) {
                    Icon(Icons.Default.SkipPrevious, null, tint = White, modifier = Modifier.size(44.dp))
                }
                
                Surface(
                    modifier = Modifier.size(84.dp),
                    shape = CircleShape,
                    color = LimeGreen,
                    onClick = onTogglePlay,
                    shadowElevation = 15.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = DeepSpace, modifier = Modifier.size(48.dp))
                    }
                }

                IconButton(onClick = onNext, modifier = Modifier.size(56.dp)) {
                    Icon(Icons.Default.SkipNext, null, tint = White, modifier = Modifier.size(44.dp))
                }
            }

            Spacer(modifier = Modifier.weight(0.6f))

            // Premium: Ecualizador
            if (isPremium) {
                FuturisticEqualizer()
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(60.dp).clickable { },
                    color = White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, White.copy(alpha = 0.1f))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, null, tint = LimeGreen, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("DESBLOQUEAR ECUALIZADOR PRO", color = White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun FuturisticEqualizer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, LimeGreen.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.GraphicEq, null, tint = LimeGreen, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Text("NÚCLEO DE ECUALIZACIÓN", color = White, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(20) {
                val heightFactor by rememberInfiniteTransition().animateFloat(
                    initialValue = 0.1f, targetValue = 1f,
                    animationSpec = infiniteRepeatable(tween(Random.nextInt(400, 1000)), RepeatMode.Reverse)
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight(heightFactor)
                        .background(LimeGreen, RoundedCornerShape(2.dp))
                )
            }
        }
    }
}
