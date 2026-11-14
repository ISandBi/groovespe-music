package com.ejemplo.grovespemusic.ui.screens

import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ejemplo.grovespemusic.R
import com.ejemplo.grovespemusic.model.Band
import com.ejemplo.grovespemusic.model.Song
import com.ejemplo.grovespemusic.model.User
import com.ejemplo.grovespemusic.model.UserRole
import com.ejemplo.grovespemusic.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandDetailScreen(
    bandId: String,
    currentUser: User,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    var isPlayingGlobal by remember { mutableStateOf(false) }
    var currentTrackId by remember { mutableStateOf("") }
    
    // Identificador dinámico para el audio (AUDIO-PRUEBA.wav)
    // Nota: Los nombres de recursos en Android se normalizan a minúsculas y guiones bajos.
    val audioResId = remember {
        val id = context.resources.getIdentifier("audio_prueba", "drawable", context.packageName)
        if (id == 0) context.resources.getIdentifier("AUDIO-PRUEBA", "drawable", context.packageName) else id
    }

    // MediaPlayer instanciado con el recurso .wav
    val mediaPlayer = remember {
        if (audioResId != 0) {
            try {
                MediaPlayer.create(context, audioResId).apply {
                    isLooping = true
                }
            } catch (e: Exception) { null }
        } else null
    }

    // Liberar recursos al salir
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    // Control de reproducción
    LaunchedEffect(isPlayingGlobal) {
        if (isPlayingGlobal) {
            mediaPlayer?.start()
        } else {
            mediaPlayer?.pause()
        }
    }

    // Simulación de carga futurista con transición
    var isSyncing by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(600)
        isSyncing = false
    }

    val band = remember {
        Band(
            id = bandId,
            name = "Cyber Chilangos",
            genre = "Quantum Jazz / Cyber-Core",
            description = "Emanando desde los sectores cuánticos de la CDMX. Los Chilangos fusionan la psicodelia del tráfico en Reforma con algoritmos de jazz experimental. Una frecuencia 100% verificada en la red GrooveSpace.",
            imageUrl = "https://images.unsplash.com/photo-1514525253361-bee8718a74a2?q=80&w=1200",
            monthlyListeners = 124500,
            totalEarnings = 56600.80,
            songs = listOf(
                Song("1", "Tráfico en Reforma 2099", "4:20", 125000),
                Song("2", "Tacos de Aliento Solar", "3:45", 98000),
                Song("3", "Metro Pantitlán Zero", "5:10", 87000),
                Song("4", "Lluvia de Neón", "4:05", 72000),
                Song("5", "Noche en la Juárez", "3:55", 54000)
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
        if (isSyncing) {
            CyberLoadingScreen()
        } else {
            // 1. Imagen de Fondo con Blur Dinámico
            AsyncImage(
                model = band.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .graphicsLayer {
                        translationY = -scrollState.firstVisibleItemScrollOffset.toFloat() / 2.5f
                    }
                    .blur(if (scrollState.firstVisibleItemIndex > 0) 40.dp else 0.dp)
                    .alpha(0.4f),
                contentScale = ContentScale.Crop
            )

            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(headerHeight)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, DeepSpace),
                                    startY = headerHeightPx * 0.4f
                                )
                            ),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(32.dp)
                                .padding(bottom = 48.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val infiniteTransition = rememberInfiniteTransition(label = "neon")
                                val glow by infiniteTransition.animateFloat(
                                    initialValue = 0.4f, targetValue = 1f,
                                    animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "glow"
                                )
                                Box(modifier = Modifier.size(10.dp).alpha(glow).clip(CircleShape).background(LimeGreen).border(2.dp, Color.White, CircleShape))
                                Spacer(Modifier.width(12.dp))
                                Text("CDMX CYBER-CORE LIVE", color = LimeGreen, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                            }
                            Text(
                                text = band.name.uppercase(),
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 66.sp,
                                letterSpacing = (-4).sp,
                                modifier = Modifier.graphicsLayer {
                                    val factor = (scrollState.firstVisibleItemScrollOffset.toFloat() / 10f)
                                    translationX = -factor
                                }
                            )
                            Text(
                                text = "${band.monthlyListeners} OYENTES INTERSTELARES",
                                color = GrayText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }

                // 2. Barra de Acciones Neón (Interactiva y Vibrante)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var following by remember { mutableStateOf(true) }
                        Surface(
                            onClick = { following = !following },
                            color = if (following) LimeGreen.copy(alpha = 0.15f) else Color.Transparent,
                            border = BorderStroke(1.dp, if (following) LimeGreen else GrayText),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text("CONECTADO", modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), color = if (following) LimeGreen else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(Icons.Default.Favorite, null, tint = LimeGreen, modifier = Modifier.size(36.dp))
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        // Botón de Reproducción con Efecto de Energía
                        val infiniteTransition = rememberInfiniteTransition(label = "energy")
                        val energyScale by infiniteTransition.animateFloat(
                            initialValue = 1f, targetValue = 1.15f,
                            animationSpec = infiniteRepeatable(tween(800, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "scale"
                        )

                        Box(contentAlignment = Alignment.Center) {
                            Box(modifier = Modifier.size(70.dp).scale(energyScale).blur(20.dp).background(LimeGreen.copy(alpha = 0.3f), CircleShape))
                            Surface(
                                modifier = Modifier.size(64.dp),
                                shape = CircleShape,
                                color = LimeGreen,
                                shadowElevation = 20.dp,
                                onClick = { 
                                    isPlayingGlobal = !isPlayingGlobal 
                                    if (currentTrackId.isEmpty()) currentTrackId = band.songs.first().id
                                }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isPlayingGlobal) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = DeepSpace,
                                        modifier = Modifier.size(40.dp)
                                    )
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

                // 4. Sección de Tracks
                item {
                    Text("FRECUENCIAS ACTIVAS", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 24.dp, top = 40.dp, bottom = 20.dp), letterSpacing = 3.sp)
                }

                itemsIndexed(band.songs) { index, song ->
                    val isActive = currentTrackId == song.id
                    val colorAnim by animateColorAsState(if (isActive) LimeGreen else Color.White, label = "color")
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 14.dp)
                            .background(if (isActive) LimeGreen.copy(alpha = 0.05f) else Color.Transparent, RoundedCornerShape(12.dp))
                            .clickable { 
                                currentTrackId = song.id
                                isPlayingGlobal = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = (index + 1).toString().padStart(2, '0'), 
                            color = if (isActive) LimeGreen else GrayText, 
                            modifier = Modifier.width(36.dp), 
                            fontSize = 11.sp, 
                            fontWeight = FontWeight.Black
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isActive) LimeGreen.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.05f))
                                .border(1.dp, if (isActive) LimeGreen.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isActive && isPlayingGlobal) Icons.Default.Equalizer else Icons.Default.MusicNote,
                                contentDescription = null,
                                tint = if (isActive) LimeGreen else White.copy(alpha = 0.4f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                            Text(song.title, color = colorAnim, fontWeight = FontWeight.Bold, fontSize = 18.sp, maxLines = 1)
                            Text("${song.streamCount} HITS CYBER", color = GrayText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        
                        if (isActive && isPlayingGlobal) {
                            Icon(Icons.Default.GraphicEq, null, tint = LimeGreen, modifier = Modifier.size(20.dp))
                        } else {
                            Text(song.duration, color = GrayText, fontSize = 13.sp)
                        }
                    }
                }

                // 5. Acerca de (Bio Futurista)
                item {
                    Box(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(32.dp))
                            .background(Color.White.copy(alpha = 0.03f))
                            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(32.dp))
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text("MANIFIESTO ARTÍSTICO", color = LimeGreen, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = band.description,
                                color = Color.White.copy(alpha = 0.8f),
                                lineHeight = 28.sp,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify
                            )
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(140.dp)) }
            }
        }

        // Cyber Sticky TopBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(DeepSpace.copy(alpha = toolbarAlpha))
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .background(Color.Black.copy(alpha = if (toolbarAlpha < 0.4f) 0.2f else 0f), CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            if (toolbarAlpha > 0.8f) {
                Text(band.name.uppercase(), color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp, modifier = Modifier.align(Alignment.Center), letterSpacing = 2.sp)
            }
        }
    }
}

@Composable
fun CyberLoadingScreen() {
    Box(Modifier.fillMaxSize().background(DeepSpace), contentAlignment = Alignment.Center) {
        val infiniteTransition = rememberInfiniteTransition(label = "loading")
        val rotate by infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)), label = "rotate"
        )
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer { rotationZ = rotate }
                    .drawWithContent {
                        drawContent()
                        drawCircle(
                            Brush.sweepGradient(listOf(LimeGreen, Color.Transparent, LimeGreen)),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
                        )
                    }
            )
            Spacer(Modifier.height(32.dp))
            Text("SINCRONIZANDO SECTOR CDMX...", color = LimeGreen, fontSize = 10.sp, letterSpacing = 3.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun ArtistPrivateDashboard(earnings: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, LimeGreen.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(28.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.TrendingUp, null, tint = LimeGreen, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text("DASHBOARD ARTISTA PRO", color = LimeGreen, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            }
            Spacer(Modifier.height(24.dp))
            Text("GANANCIAS ACUMULADAS", color = GrayText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text("$${earnings} MXN", color = Color.White, fontSize = 44.sp, fontWeight = FontWeight.Black)
            
            Spacer(Modifier.height(24.dp))
            LinearProgressIndicator(
                progress = { 0.85f }, 
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape), 
                color = LimeGreen, 
                trackColor = Color.White.copy(alpha = 0.1f)
            )
            Spacer(Modifier.height(12.dp))
            Text("PRÓXIMO DEPÓSITO AUTOMÁTICO: 01/11/2099", color = LimeGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
