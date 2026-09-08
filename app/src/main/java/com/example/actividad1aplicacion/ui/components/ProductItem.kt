package com.example.actividad1aplicacion.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.actividad1aplicacion.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    product: Product,
    isColorBlind: Boolean,
    onDelete: (String) -> Unit,
    onFavoriteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete(product.id)
                true
            } else {
                false
            }
        }
    )

    val offset by animateDpAsState(
        targetValue = if (product.isAdded) 0.dp else 50.dp,
        label = "offsetAnimation"
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = when (dismissState.targetValue) {
                SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF5252).copy(alpha = 0.8f)
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 6.dp)
                    .background(color, RoundedCornerShape(16.dp))
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar artículo",
                    tint = Color.White
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .offset(y = offset)
                    .semantics {
                        contentDescription = "Artículo: ${product.name}. Desliza a la izquierda para eliminar."
                    }
            ) {
                ProductCard(
                    product = product,
                    isColorBlind = isColorBlind,
                    onFavoriteClick = onFavoriteClick,
                    onEditClick = onEditClick,
                    onDeleteClick = { onDelete(product.id) }
                )
            }
        }
    )
}
