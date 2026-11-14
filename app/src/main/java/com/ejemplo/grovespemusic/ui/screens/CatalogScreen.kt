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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ejemplo.grovespemusic.model.Band
import com.ejemplo.grovespemusic.model.Song
import com.ejemplo.grovespemusic.ui.theme.*

@Composable
fun CatalogScreen(
    onBandClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    currentSong: Song?,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onOpenPlayer: () -> Unit
) {
    val bands = remember {
        listOf(
            Band(id = "1", name = "Cyber Chilangos", genre = "Cyber Fusion", description = "", imageUrl = "https://images.unsplash.com/photo-1514525253361-bee8718a74a2?q=80&w=600", monthlyListeners = 15420),
            Band(id = "2", name = "Eco de Ciudad", genre = "Indie Neo-Rock", description = "", imageUrl = "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?q=80&w=600", monthlyListeners = 12800)
        )
    }

    Scaffold(
        containerColor = DeepSpace,
        bottomBar = {
            if (currentSong != null) {
                MiniPlayer(
                    song = currentSong,
                    isPlaying = isPlaying,
                    onTogglePlay = onTogglePlay,
                    onClick = onOpenPlayer
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(SoftPurple.copy(alpha = 0.2f), DeepSpace)))
                .verticalScroll(rememberScrollState())
        ) {
            HomeHeader(onProfileClick)
            QuickFilters()
            
            Text("LO MEJOR DE LA RED CDMX", color = White, fontSize = 20.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(20.dp))
            
            LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(bands) { band ->
                    BandCard(band, onBandClick)
                }
            }
        }
    }
}

@Composable
fun MiniPlayer(song: Song, isPlaying: Boolean, onTogglePlay: () -> Unit, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(64.dp)
            .clickable { onClick() },
        color = CardBackground.copy(alpha = 0.95f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, LimeGreen.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1511192336575-5a79af67a629?q=80&w=100",
                contentDescription = null,
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(song.title, color = White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("Cyber Chilangos", color = LimeGreen, fontSize = 11.sp, maxLines = 1)
            }
            IconButton(onClick = onTogglePlay) {
                Icon(if (isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled, null, tint = LimeGreen, modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Composable
fun BandCard(band: Band, onClick: (String) -> Unit) {
    Column(modifier = Modifier.width(160.dp).clickable { onClick(band.id) }) {
        AsyncImage(
            model = band.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(160.dp).clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Text(band.name, color = White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        Text(band.genre, color = GrayText, fontSize = 12.sp)
    }
}

@Composable
fun HomeHeader(onProfileClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(20.dp).statusBarsPadding(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("Explorar", color = White, fontSize = 28.sp, fontWeight = FontWeight.Black)
        IconButton(onClick = onProfileClick) { Icon(Icons.Default.AccountCircle, null, tint = White, modifier = Modifier.size(32.dp)) }
    }
}

@Composable
fun QuickFilters() {
    Row(modifier = Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Música", "Podcasts", "CDMX Live").forEach { 
            Surface(color = White.copy(alpha = 0.1f), shape = CircleShape) {
                Text(it, color = White, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontSize = 12.sp)
            }
        }
    }
}
