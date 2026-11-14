package com.ejemplo.grovespemusic.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.grovespemusic.model.User
import com.ejemplo.grovespemusic.model.UserRole
import com.ejemplo.grovespemusic.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onRoleToggle: () -> Unit
) {
    var name by remember { mutableStateOf(user.name) }
    var bio by remember { mutableStateOf("Amante de la escena local de CDMX.") }
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = DeepSpace,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PERFIL GALÁCTICO", letterSpacing = 2.sp, fontWeight = FontWeight.Black, fontSize = 14.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent, 
                    titleContentColor = LimeGreen
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Futuristic Avatar with Glow
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(LimeGreen.copy(alpha = 0.2f), Color.Transparent)))
                    .padding(4.dp)
                    .border(2.dp, LimeGreen.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(DarkPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(60.dp))
                }
                
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(LimeGreen, CircleShape)
                        .size(36.dp)
                        .border(2.dp, DeepSpace, CircleShape)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = DeepSpace, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // User Info
            FuturisticTextField(value = name, onValueChange = { name = it }, label = "Tu Nombre de Escena")
            Spacer(modifier = Modifier.height(16.dp))
            FuturisticTextField(value = bio, onValueChange = { bio = it }, label = "Biografía", singleLine = false)

            Spacer(modifier = Modifier.height(40.dp))

            // Role Toggle Card (Futuristic Glassmorphism)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardBackground.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, LimeGreen.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "MODO DE CUENTA",
                        color = GrayText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                if (user.role == UserRole.FAN) "FAN DE LA ESCENA" else "ARTISTA GROOVESPACE",
                                color = White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                if (user.role == UserRole.FAN) "Descubre bandas locales" else "Gestiona tu carrera musical",
                                color = LimeGreen,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = user.role == UserRole.ARTIST,
                            onCheckedChange = { onRoleToggle() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = DeepSpace,
                                checkedTrackColor = LimeGreen,
                                uncheckedThumbColor = GrayText,
                                uncheckedTrackColor = DarkPurple
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red.copy(alpha = 0.7f))
            ) {
                Text("DESCONECTARSE DEL ESTUDIO", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FuturisticTextField(value: String, onValueChange: (String) -> Unit, label: String, singleLine: Boolean = true) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(), 
            color = LimeGreen, 
            fontSize = 10.sp, 
            fontWeight = FontWeight.Black, 
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
            letterSpacing = 1.sp
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = singleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LimeGreen,
                unfocusedBorderColor = GrayText.copy(alpha = 0.3f),
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
            )
        )
    }
}
