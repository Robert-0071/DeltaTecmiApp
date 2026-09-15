package com.example.actividad1aplicacion.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad1aplicacion.R
import com.example.actividad1aplicacion.ui.components.SwipeableNoteItem
import com.example.notemanager.data.local.NoteEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    notes: List<NoteEntity>,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDeleteNote: (NoteEntity) -> Unit,
    onToggleComplete: (NoteEntity) -> Unit,
    onBackToHome: () -> Unit,
    onGoToFavs: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_login_svg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                CustomBottomNav(
                    isFavoritesActive = false,
                    isNotesActive = true,
                    onHomeClick = onBackToHome,
                    onFavClick = onGoToFavs,
                    onNotesClick = { },
                    onAddClick = onAddNoteClick
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Mis Notas",
                            color = Color(0xFFE5AA27),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${notes.size} notas guardadas",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 14.sp
                        )
                    }
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFFE5AA27))
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(notes, key = { it.id }) { note ->
                        SwipeableNoteItem(
                            note = note,
                            onDelete = onDeleteNote,
                            onToggleComplete = { onToggleComplete(note) }
                        )
                    }
                }
            }
        }
    }
}
