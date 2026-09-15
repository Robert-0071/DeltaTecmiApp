package com.example.actividad1aplicacion.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.actividad1aplicacion.ui.components.NoteCard
import com.example.notemanager.data.local.NoteEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveNoteScreen(
    notes: List<NoteEntity>,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDeleteNote: (NoteEntity) -> Unit,
    onToggleComplete: (NoteEntity) -> Unit,
    onBackToHome: () -> Unit
) {
    var selectedNote by remember { mutableStateOf<NoteEntity?>(null) }

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
                    onFavClick = { },
                    onNotesClick = { },
                    onAddClick = onAddNoteClick
                )
            }
        ) { padding ->
            Row(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // COLUMNA IZQUIERDA
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight()
                        .padding(start = 16.dp, top = 8.dp)
                ) {
                    Text(
                        text = "Mis Notas",
                        color = Color(0xFFE5AA27),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(notes, key = { it.id }) { note ->
                            NoteCard(
                                note = note,
                                modifier = Modifier.clickable { selectedNote = note },
                                onToggleComplete = { onToggleComplete(note) }
                            )
                        }
                    }
                }

                // COLUMNA DERECHA
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
                        .padding(24.dp)
                ) {
                    if (selectedNote != null) {
                        Text(
                            text = selectedNote!!.title,
                            color = Color(0xFFE5AA27),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = selectedNote!!.content,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 16.sp
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Button(
                            onClick = { 
                                onDeleteNote(selectedNote!!)
                                selectedNote = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252).copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("ELIMINAR ESTA NOTA", color = Color(0xFFFF5252))
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Selecciona una nota para ver detalles", color = Color.White.copy(alpha = 0.3f))
                        }
                    }
                }
            }
        }
    }
}
