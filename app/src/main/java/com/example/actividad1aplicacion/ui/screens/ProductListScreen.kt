package com.example.actividad1aplicacion.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad1aplicacion.R
import com.example.actividad1aplicacion.model.Product
import com.example.actividad1aplicacion.ui.components.ProductItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    products: List<Product>,
    isColorBlind: Boolean,
    onAddClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDeleteProduct: (String) -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit,
    onEditClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isShowingFavorites by remember { mutableStateOf(false) }

    val filteredList = products.filter { product ->
        val matchesTab = if (isShowingFavorites) product.isFavorite else true
        val matchesSearch = product.name.contains(searchQuery, ignoreCase = true) ||
                (product.store?.contains(searchQuery, ignoreCase = true) ?: false)
        matchesTab && matchesSearch
    }

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
                    isFavoritesActive = isShowingFavorites,
                    onHomeClick = { isShowingFavorites = false },
                    onFavClick = { isShowingFavorites = true },
                    onAddClick = onAddClick
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
                            text = if (isShowingFavorites) "Favoritos" else "Delta Tecmi",
                            color = Color(0xFFE5AA27),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${filteredList.size} artículos",
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

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(56.dp),
                    placeholder = { Text("Buscar por nombre, tienda o precio...", color = Color.White.copy(alpha = 0.3f), fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.3f)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.05f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem(R.drawable.ic_trend_up, "Subió", if(isColorBlind) Color(0xFFFF9800) else Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.width(16.dp))
                    LegendItem(R.drawable.ic_trend_down, "Bajó", if(isColorBlind) Color(0xFF4FC3F7) else Color(0xFFF44336))
                    Spacer(modifier = Modifier.width(16.dp))
                    LegendItem(R.drawable.ic_trend_neutral, "Igual", Color(0xFF607D8B))
                    Spacer(modifier = Modifier.weight(1f))
                    Text("— desliza para eliminar", color = Color.White.copy(alpha = 0.3f), fontSize = 10.sp)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(filteredList, key = { it.id }) { product ->
                        ProductItem(
                            product = product,
                            isColorBlind = isColorBlind,
                            onDelete = onDeleteProduct,
                            onFavoriteClick = { onFavoriteClick(product.id, product.isFavorite) },
                            onEditClick = { onEditClick(product.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(icon: Int, label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = icon), contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
        Text(text = label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun CustomBottomNav(
    isFavoritesActive: Boolean,
    onHomeClick: () -> Unit,
    onFavClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth().height(90.dp)) {
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(72.dp).background(Color(0xFF0D1B3E)).padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = if (isFavoritesActive) R.drawable.ic_star_filled else R.drawable.ic_star_outline,
                label = "Favoritos",
                isActive = isFavoritesActive,
                onClick = onFavClick
            )
            Spacer(modifier = Modifier.width(64.dp))
            NavItem(
                icon = R.drawable.ic_home,
                label = "Inicio",
                isActive = !isFavoritesActive,
                onClick = onHomeClick
            )
        }
        Box(
            modifier = Modifier.align(Alignment.TopCenter).size(64.dp).clip(CircleShape).background(Brush.linearGradient(colors = listOf(Color(0xFFFAD961), Color(0xFFF76B1C)))).clickable { onAddClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF0A1830), modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun NavItem(icon: Int, label: String, isActive: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Icon(painter = painterResource(id = icon), contentDescription = null, tint = if (isActive) Color(0xFFE5AA27) else Color.White.copy(alpha = 0.3f), modifier = Modifier.size(24.dp))
        Text(text = label, color = if (isActive) Color(0xFFE5AA27) else Color.White.copy(alpha = 0.3f), fontSize = 10.sp)
    }
}
