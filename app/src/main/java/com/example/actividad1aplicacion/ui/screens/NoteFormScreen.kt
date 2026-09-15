package com.example.actividad1aplicacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormScreen(
    onSaveNote: (String, String, String?) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var contentError by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    // Requisito Paso 7: Validación con LaunchedEffect
    LaunchedEffect(title, content, isSubmitted) {
        if (isSubmitted) {
            titleError = if (title.isBlank()) "¡Título requerido!" else null
            contentError = if (content.isBlank()) "¡Contenido requerido!" else null
        }
    }

    Scaffold(
        containerColor = Color(0xFF0A1830),
        topBar = {
            TopAppBar(
                title = { Text("Nueva Nota", color = Color(0xFFE5AA27), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color(0xFFE5AA27))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            InputField(
                label = "TÍTULO *",
                value = title,
                onValueChange = { title = it },
                error = titleError,
                hint = "Título de la nota"
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                label = { Text("CONTENIDO *", color = Color(0xFFBFE0FF), fontSize = 12.sp) },
                placeholder = { Text("Escribe aquí tu nota...", color = Color.White.copy(alpha = 0.2f)) },
                isError = contentError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE5AA27),
                    unfocusedBorderColor = if (contentError != null) Color(0xFFFF5252) else Color.White.copy(alpha = 0.2f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
            if (contentError != null) {
                Text(text = contentError!!, color = Color(0xFFFF5252), fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp, start = 8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para añadir imagen (Requisito Gesto Zoom)
            Button(
                onClick = { imageUri = "https://picsum.photos/800/600" }, // Imagen aleatoria de prueba
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if(imageUri == null) "🖼️ AÑADIR IMAGEN" else "✅ IMAGEN AÑADIDA", color = Color.White)
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    isSubmitted = true
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSaveNote(title, content, imageUri)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5AA27)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("GUARDAR NOTA", color = Color(0xFF0A1830), fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Cancelar", color = Color.White.copy(alpha = 0.5f))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
