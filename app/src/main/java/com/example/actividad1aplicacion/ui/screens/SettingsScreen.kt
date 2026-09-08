package com.example.actividad1aplicacion.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad1aplicacion.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isColorBlind: Boolean,
    isTalkBack: Boolean,
    onColorBlindToggle: (Boolean) -> Unit,
    onTalkBackToggle: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFF0A1830),
        topBar = {
            TopAppBar(
                title = { Text("Configuración", color = Color(0xFFE5AA27), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color(0xFFE5AA27))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 24.dp)) {
            
            Spacer(modifier = Modifier.height(24.dp))

            // Modo Daltonismo
            SettingsCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Modo Daltonismo", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Usa azul ▲ y naranja ▼ en lugar de verde y rojo", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                    }
                    Switch(
                        checked = isColorBlind,
                        onCheckedChange = onColorBlindToggle,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE5AA27))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Modo TalkBack
            SettingsCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Modo TalkBack", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Describe en voz alta los elementos y acciones", color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                    }
                    Switch(
                        checked = isTalkBack,
                        onCheckedChange = onTalkBackToggle,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFE5AA27))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Leyenda Preview
            SettingsCard {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    TrendPreview("Subió", R.drawable.ic_trend_up, if(isColorBlind) Color(0xFFFF9800) else Color(0xFF4CAF50))
                    TrendPreview("Bajó", R.drawable.ic_trend_down, if(isColorBlind) Color(0xFF4FC3F7) else Color(0xFFF44336))
                    TrendPreview("Igual", R.drawable.ic_trend_neutral, Color(0xFF607D8B))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Cerrar Sesión
            SettingsCard(
                modifier = Modifier.clickable { onLogout() }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFFF5252))
                    Text("Cerrar Sesión", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0x33102F62)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            content()
        }
    }
}

@Composable
fun TrendPreview(label: String, icon: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painter = painterResource(id = icon), contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
    }
}
