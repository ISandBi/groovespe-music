package com.ejemplo.grovespemusic.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ejemplo.grovespemusic.model.Band
import com.ejemplo.grovespemusic.model.Song
import com.ejemplo.grovespemusic.model.User
import com.ejemplo.grovespemusic.model.UserRole
import com.ejemplo.grovespemusic.ui.theme.*

@Composable
fun BandDetailScreen(
    bandId: String,
    currentUser: User,
    currentTrackId: String,
    isPlayingGlobal: Boolean,
    onBack: () -> Unit,
    onPlaySong: (Song) -> Unit
) {
    val scrollState = rememberLazyListState()

    val band = remember {
        Band(
            id = bandId,
            name = "Cyber Chilangos",
            genre = "Quantum Jazz / Cyber-Core",
            description = "Emanando desde los sectores cuánticos de la CDMX. Fusionamos la psicodelia del tráfico en Reforma con algoritmos de jazz experimental del siglo XXII. Una frecuencia 100% verificada en la red GrooveSpace.",
            imageUrl = "https://images.unsplash.com/photo-1514525253361-bee8718a74a2?q=80&w=1200",
            monthlyListeners = 284500,
            totalEarnings = 92600.80,
            songs = listOf(
                Song("1", "Tráfico en Reforma 2099", "4:20", 125000),
                Song("2", "Tacos de Aliento Solar", "3:45", 98000),
                Song("3", "Metro Pantitlán Zero", "5:10", 87000),
                Song("4", "Lluvia de Neón", "4:05", 72000)
            )
        )
    }

    val headerHeight = 500.dp
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarAlpha by remember {
        derivedStateOf {
            val offset = scrollState.firstVisibleItemScrollOffset.toFloat()
            if (scrollState.firstVisibleItemIndex > 0) 1f
            else (offset / (headerHeightPx * 0.4f)).coerceIn(0f, 1f)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepSpace)) {
        // 1. Hero Image con Paralaje
        AsyncImage(
            model = band.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .graphicsLayer { translationY = -scrollState.firstVisibleItemScrollOffset.toFloat() / 2.5f }
                .blur(if (scrollState.firstVisibleItemIndex > 0) 30.dp else 0.dp)
                .alpha(0.5f),
            contentScale = ContentScale.Crop
        )

        LazyColumn(state = scrollState, modifier = Modifier.fillMaxSize()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(headerHeight)
                        .background(Brush.verticalGradient(colors = listOf(Color.Transparent, DeepSpace), startY = headerHeightPx * 0.4f)),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column(modifier = Modifier.padding(32.dp).padding(bottom = 48.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val glow by rememberInfiniteTransition(label = "glow").animateFloat(
                                initialValue = 0.4f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                                label = "glow"
                            )
                            Box(modifier = Modifier.size(10.dp).alpha(glow).clip(CircleShape).background(LimeGreen).border(2.dp, Color.White, CircleShape))
                            Spacer(Modifier.width(12.dp))
                            Text("CDMX CYBER-CORE ACTIVA", color = LimeGreen, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                        }
                        Text(
                            text = band.name.uppercase(),
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            lineHeight = 66.sp,
                            letterSpacing = (-4).sp
                        )
                        Text(text = "${band.monthlyListeners} OYENTES INTERSTELARES", color = GrayText, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            // 2. Barra de Acciones Neón (Interactiva)
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = LimeGreen.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, LimeGreen.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(2.dp)
                    ) {
                        Text("SINCRO OK", modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp), color = LimeGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(Icons.Default.Favorite, null, tint = LimeGreen, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.weight(1f))

                    // Botón Play con Aura de Energía
                    val energyScale by rememberInfiniteTransition(label = "energy").animateFloat(initialValue = 1f, targetValue = 1.2f, animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "energy")
                    Box(contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.size(75.dp).scale(energyScale).blur(25.dp).background(LimeGreen.copy(alpha = 0.3f), CircleShape))
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape,
                            color = LimeGreen,
                            shadowElevation = 20.dp,
                            onClick = { 
                                if (currentTrackId.isEmpty()) {
                                    onPlaySong(band.songs.first())
                                } else {
                                    onPlaySong(band.songs.find { it.id == currentTrackId } ?: band.songs.first())
                                }
                            }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = if (isPlayingGlobal && currentTrackId.isNotEmpty()) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, tint = DeepSpace, modifier = Modifier.size(40.dp))
                            }
                        }
                    }
                }
            }

            // 3. Panel de Ganancias - SOLO PARA ARTISTAS
            if (currentUser.role == UserRole.ARTIST) {
                item {
                    ArtistPrivateDashboard(band.totalEarnings)
                }
            }

            // 4. Tracks Section con Visualizador
            item {
                Text("FRECUENCIAS ACTIVAS", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 24.dp, top = 40.dp, bottom = 20.dp), letterSpacing = 4.sp)
            }

            itemsIndexed(band.songs) { index, song ->
                val isActive = currentTrackId == song.id
                val colorAnim by animateColorAsState(if (isActive) LimeGreen else Color.White, label = "color")
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 14.dp)
                        .background(if (isActive) LimeGreen.copy(alpha = 0.08f) else Color.Transparent, RoundedCornerShape(12.dp))
                        .clickable { onPlaySong(song) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = (index + 1).toString().padStart(2, '0'), color = if (isActive) LimeGreen else GrayText, modifier = Modifier.width(40.dp), fontSize = 11.sp, fontWeight = FontWeight.Black)
                    Box(modifier = Modifier.size(52.dp).clip(RoundedCornerShape(12.dp)).background(if (isActive) LimeGreen.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f)).border(1.dp, if (isActive) LimeGreen.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                        if (isActive && isPlayingGlobal) {
                            CyberVisualizer()
                        } else {
                            Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = if (isActive) LimeGreen else Color.White.copy(alpha = 0.4f), modifier = Modifier.size(24.dp))
                        }
                    }
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 20.dp)) {
                        Text(song.title, color = colorAnim, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 1)
                        Text("${song.streamCount} HITS CYBER", color = GrayText, fontSize = 11.sp)
                    }
                    if (isActive && isPlayingGlobal) Icon(Icons.Default.GraphicEq, null, tint = LimeGreen, modifier = Modifier.size(20.dp))
                    else Text(song.duration, color = GrayText, fontSize = 13.sp)
                }
            }

            // 5. Bio
            item {
                Box(modifier = Modifier.padding(24.dp).fillMaxWidth().clip(RoundedCornerShape(32.dp)).background(Color.White.copy(alpha = 0.03f)).border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(32.dp))) {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Text("MANIFIESTO ARTÍSTICO", color = LimeGreen, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                        Spacer(Modifier.height(20.dp))
                        Text(text = band.description, color = Color.White.copy(alpha = 0.8f), lineHeight = 28.sp, fontSize = 16.sp)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(140.dp)) }
        }

        // Cyber Sticky TopBar
        Box(modifier = Modifier.fillMaxWidth().height(110.dp).background(DeepSpace.copy(alpha = toolbarAlpha)).statusBarsPadding(), contentAlignment = Alignment.Center) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp).background(Color.Black.copy(alpha = if (toolbarAlpha < 0.4f) 0.2f else 0f), CircleShape)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            if (toolbarAlpha > 0.8f) {
                Text(band.name.uppercase(), color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp, letterSpacing = 4.sp)
            }
        }
    }
}

@Composable
fun CyberVisualizer() {
    Row(modifier = Modifier.fillMaxSize().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        val infiniteTransition = rememberInfiniteTransition(label = "h")
        repeat(4) { index ->
            val h by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(400 + (index * 100)), RepeatMode.Reverse),
                label = "h"
            )
            Box(modifier = Modifier.width(3.dp).fillMaxHeight(h).background(LimeGreen, RoundedCornerShape(1.dp)))
        }
    }
}

@Composable
fun ArtistPrivateDashboard(earnings: Double) {
    Card(modifier = Modifier.fillMaxWidth().padding(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)), shape = RoundedCornerShape(24.dp), border = BorderStroke(1.dp, LimeGreen.copy(alpha = 0.2f))) {
        Column(modifier = Modifier.padding(28.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = LimeGreen, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text("DASHBOARD ARTISTA PRO", color = LimeGreen, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(24.dp))
            Text("GANANCIAS ACUMULADAS", color = GrayText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text("$${earnings} MXN", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(24.dp))
            LinearProgressIndicator(progress = { 0.85f }, modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape), color = LimeGreen, trackColor = Color.White.copy(alpha = 0.1f))
        }
    }
}

@Composable
fun CyberLoadingScreen() {
    Box(Modifier.fillMaxSize().background(DeepSpace), contentAlignment = Alignment.Center) {
        val rotate by rememberInfiniteTransition(label = "loading").animateFloat(initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)), label = "rotate")
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(80.dp).graphicsLayer { rotationZ = rotate }.drawWithContent {
                drawContent()
                drawCircle(Brush.sweepGradient(listOf(LimeGreen, Color.Transparent, LimeGreen)), style = Stroke(width = 4.dp.toPx()))
            })
            Spacer(Modifier.height(24.dp))
            Text("SINCRONIZANDO SECTOR CDMX...", color = LimeGreen, fontSize = 10.sp, letterSpacing = 3.sp, fontWeight = FontWeight.Black)
        }
    }
}
