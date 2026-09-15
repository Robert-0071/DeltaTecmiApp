package com.example.actividad1aplicacion.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad1aplicacion.R
import com.example.actividad1aplicacion.model.Product
import com.example.actividad1aplicacion.model.Trend

@Composable
fun ProductCard(
    product: Product,
    isColorBlind: Boolean,
    onFavoriteClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .animateContentSize()
            .semantics {
                contentDescription = if (isExpanded) {
                    "Tarjeta de ${product.name}, desplegada. Precio ${product.price}."
                } else {
                    "Tarjeta de ${product.name}, colapsada. Toca para ver detalles."
                }
            }
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(containerColor = Color(0x33102F62)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Icono de Tendencia (con lógica de daltonismo del lab)
                val trendIcon = when (product.trend) {
                    Trend.UP -> R.drawable.ic_trend_up
                    Trend.DOWN -> R.drawable.ic_trend_down
                    Trend.NEUTRAL -> R.drawable.ic_trend_neutral
                }
                val trendColor = when (product.trend) {
                    Trend.UP -> if (isColorBlind) Color(0xFFFF9800) else Color(0xFF4CAF50)
                    Trend.DOWN -> if (isColorBlind) Color(0xFF4FC3F7) else Color(0xFFF44336)
                    Trend.NEUTRAL -> Color(0xFF607D8B)
                }

                Icon(
                    painter = painterResource(id = trendIcon),
                    contentDescription = null,
                    tint = trendColor,
                    modifier = Modifier.size(24.dp)
                )

                // Info
                Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                    Text(text = product.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    if (product.store != null) {
                        Text(text = product.store, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
                    }
                }

                Text(text = product.price, color = Color(0xFFE5AA27), fontSize = 18.sp, fontWeight = FontWeight.Bold)

                IconButton(onClick = onFavoriteClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        painter = painterResource(id = if (product.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline),
                        contentDescription = "Favorito",
                        tint = if (product.isFavorite) Color(0xFFE5AA27) else Color.White.copy(alpha = 0.3f)
                    )
                }

                // ICONO DE LOS TRES PUNTOS (Añadido para tu captura)
                IconButton(onClick = { isExpanded = !isExpanded }, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones",
                        tint = Color.White.copy(alpha = 0.3f)
                    )
                }
                
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(24.dp)
                )
            }

            if (isExpanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.1f))
                
                DetailRow("Precio anterior", "${product.previousPrice} MXN")
                Spacer(modifier = Modifier.height(8.dp))
                
                val currentVal = product.price.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
                val previousVal = product.previousPrice.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
                val diff = currentVal - previousVal
                val variationColor = when {
                    diff > 0 -> if (isColorBlind) Color(0xFFFF9800) else Color(0xFF4CAF50)
                    diff < 0 -> if (isColorBlind) Color(0xFF4FC3F7) else Color(0xFFF44336)
                    else -> Color(0xFF607D8B)
                }
                
                DetailRow(
                    label = "Variación",
                    value = String.format("%.2f (%.1f%%)", diff, if(previousVal != 0.0) (diff/previousVal)*100 else 0.0),
                    valueColor = variationColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // BOTONES DE ACCIÓN (Nuevos, como en las imágenes)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5AA27).copy(alpha = 0.3f))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFFE5AA27), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Editar", color = Color(0xFFE5AA27), fontSize = 14.sp)
                    }

                    Button(
                        onClick = onDeleteClick,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252).copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.3f))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar", color = Color(0xFFFF5252), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, valueColor: Color = Color.White.copy(alpha = 0.5f)) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = Color.White.copy(alpha = 0.5f), fontSize = 12.sp)
        Text(text = value, color = valueColor, fontSize = 12.sp)
    }
}
