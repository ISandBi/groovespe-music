package com.ejemplo.grovespemusic.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ejemplo.grovespemusic.model.Band
import com.ejemplo.grovespemusic.ui.theme.*

@Composable
fun CatalogScreen(onBandClick: (String) -> Unit, onProfileClick: () -> Unit) {
    val bands = remember {
        listOf(
            Band(id = "1", name = "Los Chilangos Jazz", genre = "Cyber Fusion", description = "", imageUrl = "https://images.unsplash.com/photo-1511192336575-5a79af67a629?q=80&w=600", monthlyListeners = 45000),
            Band(id = "2", name = "Eco de Ciudad", genre = "Indie Neo-Rock", description = "", imageUrl = "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?q=80&w=600", monthlyListeners = 32000),
            Band(id = "3", name = "Ritmo Condesa", genre = "Salsa Futura", description = "", imageUrl = "https://images.unsplash.com/photo-1514525253361-bee8718a74a2?q=80&w=600", monthlyListeners = 28000),
            Band(id = "4", name = "Psicodelia Tlalpan", genre = "Psy-Rock", description = "", imageUrl = "https://images.unsplash.com/photo-1459749411177-042180ceea72?q=80&w=600", monthlyListeners = 15000),
            Band(id = "5", name = "Mariachi Futuro", genre = "Synth-Mariachi", description = "", imageUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=600", monthlyListeners = 52000),
            Band(id = "6", name = "Azcapo Punk", genre = "Hardcore CDMX", description = "", imageUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?q=80&w=600", monthlyListeners = 12000)
        )
    }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Scaffold(
        containerColor = DeepSpace,
        bottomBar = { FuturisticMiniPlayer() }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Fondo con Aurora CDMX
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(SoftPurple.copy(alpha = glowAlpha), Color.Transparent),
                            center = androidx.compose.ui.geometry.Offset(0f, 0f),
                            radius = 2000f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Cyber
                CyberHomeHeader(onProfileClick)

                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { 100 }) + fadeIn(tween(1000))
                ) {
                    Column {
                        // Chips de Categoría con Neón
                        CategoryNeonRow()

                        Spacer(modifier = Modifier.height(24.dp))

                        // Grid de Acceso Rápido
                        Text(
                            "FRECUENCIAS RECIENTES",
                            color = White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                        QuickAccessCyberGrid(bands, onBandClick)

                        Spacer(modifier = Modifier.height(40.dp))

                        // Sección: Top CDMX (Horizontal)
                        SectionCyberHeader("EXPEDICIÓN SONORA", "VER TODO")
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(bands) { band ->
                                CyberAlbumCard(band, onBandClick)
                            }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        // Sección: Artistas Verificados
                        SectionCyberHeader("ARTISTAS DE LA RED", null)
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            items(bands.shuffled()) { band ->
                                CyberArtistCircle(band, onBandClick)
                            }
                        }

                        Spacer(modifier = Modifier.height(140.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryNeonRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        listOf("Música", "Podcasts", "CDMX Directo", "Eventos").forEach { text ->
            Surface(
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, White.copy(alpha = 0.1f)),
                modifier = Modifier.clickable { }
            ) {
                Text(
                    text = text,
                    color = White,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun CyberHomeHeader(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("BIENVENIDO,", color = GrayText, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("EXPLORADOR", color = White, fontSize = 28.sp, fontWeight = FontWeight.Black)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) { Icon(Icons.Outlined.Notifications, null, tint = White) }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(LimeGreen, Color(0xFF88FF00))))
                    .clickable { onProfileClick() }
                    .border(2.dp, DeepSpace, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("U", color = DeepSpace, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun QuickAccessCyberGrid(bands: List<Band>, onBandClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        for (i in bands.indices step 2) {
            Row(modifier = Modifier.fillMaxWidth()) {
                CyberQuickItem(bands[i], Modifier.weight(1f), onBandClick)
                Spacer(modifier = Modifier.width(10.dp))
                if (i + 1 < bands.size) {
                    CyberQuickItem(bands[i + 1], Modifier.weight(1f), onBandClick)
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun CyberQuickItem(band: Band, modifier: Modifier, onBandClick: (String) -> Unit) {
    Surface(
        modifier = modifier
            .height(60.dp)
            .clickable { onBandClick(band.id) },
        color = Color.White.copy(alpha = 0.05f),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.5.dp, White.copy(alpha = 0.1f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = band.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = band.name,
                color = White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SectionCyberHeader(title: String, action: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        if (action != null) {
            Text(action, color = LimeGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CyberAlbumCard(band: Band, onClick: (String) -> Unit) {
    Column(modifier = Modifier.width(160.dp).clickable { onClick(band.id) }) {
        Box(modifier = Modifier.size(160.dp)) {
            AsyncImage(
                model = band.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            // Overlay glass label
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
                color = DeepSpace.copy(alpha = 0.7f),
                shape = CircleShape
            ) {
                Icon(Icons.Default.PlayArrow, null, tint = LimeGreen, modifier = Modifier.padding(6.dp).size(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(band.name, color = White, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(band.genre + " • CDMX", color = GrayText, fontSize = 12.sp)
    }
}

@Composable
fun CyberArtistCircle(band: Band, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.width(110.dp).clickable { onClick(band.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Neon Glow
            Box(modifier = Modifier.size(116.dp).clip(CircleShape).background(Brush.sweepGradient(listOf(LimeGreen, Color.Transparent, LimeGreen))))
            AsyncImage(
                model = band.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(3.dp, DeepSpace, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(band.name, color = White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

@Composable
fun FuturisticMiniPlayer() {
    var isPlaying by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(64.dp),
        color = CardBackground.copy(alpha = 0.95f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, White.copy(alpha = 0.1f)),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1511192336575-5a79af67a629?q=80&w=100",
                contentDescription = null,
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text("TRÁFICO EN REFORMA 2099", color = White, fontSize = 13.sp, fontWeight = FontWeight.Black, maxLines = 1)
                Text("LOS CHILANGOS JAZZ", color = LimeGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            }
            IconButton(onClick = { isPlaying = !isPlaying }) {
                Icon(
                    if (isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                    null,
                    tint = LimeGreen,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}
