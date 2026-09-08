package com.example.actividad1aplicacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.actividad1aplicacion.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    product: Product? = null,
    onSaveProduct: (String, String, String) -> Unit,
    onDeleteProduct: (() -> Unit)? = null,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var store by remember { mutableStateOf(product?.store ?: "") }
    var price by remember { mutableStateOf(product?.price?.replace("$", "")?.replace(",", "") ?: "") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    LaunchedEffect(name, price, isSubmitted) {
        if (isSubmitted) {
            nameError = if (name.isBlank()) "¡Nombre requerido!" else null
            priceError = if (price.isBlank()) "¡Precio requerido!" else null
        }
    }

    Scaffold(
        containerColor = Color(0xFF0A1830),
        topBar = {
            TopAppBar(
                title = { Text(if(product == null) "Nuevo Artículo" else "Editar Artículo", color = Color(0xFFE5AA27), fontWeight = FontWeight.Bold) },
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
                label = "NOMBRE *",
                value = name,
                onValueChange = { name = it },
                error = nameError,
                hint = "Ej. Mango"
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputField(
                label = "TIENDA (opcional)",
                value = store,
                onValueChange = { store = it },
                hint = "Ej. Chedraui"
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputField(
                label = "PRECIO (MXN) *",
                value = price,
                onValueChange = { price = it },
                error = priceError,
                hint = "0.00"
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    isSubmitted = true
                    if (name.isNotBlank() && price.isNotBlank()) {
                        onSaveProduct(name, store, price)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5AA27)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if(product == null) "AGREGAR ARTÍCULO" else "GUARDAR CAMBIOS", color = Color(0xFF0A1830), fontWeight = FontWeight.Bold)
            }

            if (onDeleteProduct != null) {
                OutlinedButton(
                    onClick = onDeleteProduct,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(64.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🗑️ Eliminar artículo", fontWeight = FontWeight.Bold)
                }
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

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String? = null,
    hint: String
) {
    Column {
        Text(text = label, color = Color(0xFFBFE0FF), fontSize = 12.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            placeholder = { Text(hint, color = Color.White.copy(alpha = 0.2f)) },
            isError = error != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE5AA27),
                unfocusedBorderColor = if (error != null) Color(0xFFFF5252) else Color.White.copy(alpha = 0.2f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )
        if (error != null) {
            Text(text = error, color = Color(0xFFFF5252), fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp, start = 8.dp))
        }
    }
}
