package com.example.actividad1aplicacion.ui.components

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ZoomableImage(
    model: Any?,
    modifier: Modifier = Modifier
) {
    // Estados para controlar el Zoom y la Posición
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            // Aplicamos la transformación (Pellizco)
            .graphicsLayer(
                scaleX = maxOf(1f, scale),
                scaleY = maxOf(1f, scale),
                translationX = offset.x,
                translationY = offset.y
            )
            .transformable(state = state)
    ) {
        AsyncImage(
            model = model,
            contentDescription = "Imagen con zoom",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}
