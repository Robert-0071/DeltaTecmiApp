package com.example.actividad1aplicacion.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notemanager.data.local.NoteEntity

@Composable
fun NoteCard(
    note: NoteEntity,
    modifier: Modifier = Modifier,
    onToggleComplete: () -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize() // Requisito Fase 4 del Manual
            .semantics {
                contentDescription = if (isExpanded) {
                    "Nota ${note.title}, desplegada."
                } else {
                    "Nota ${note.title}, colapsada. Toca para ver contenido."
                }
            }
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = if (note.isCompleted) Color(0x3310B981) else Color(0x33102F62)
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (note.isCompleted) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = note.title,
                    color = if (note.isCompleted) Color(0xFF10B981) else Color(0xFFE5AA27),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                Checkbox(
                    checked = note.isCompleted,
                    onCheckedChange = { onToggleComplete() },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF10B981))
                )

                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f)
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                
                // Mostrar imagen si existe
                if (note.imageUri != null) {
                    ZoomableImage(
                        model = note.imageUri,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.2f))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = note.content,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Creada: ${note.date}",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 10.sp
                )
            }
        }
    }
}
