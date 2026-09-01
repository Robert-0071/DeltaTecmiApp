package com.example.actividad1aplicacion

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isColorBlindMode = MutableStateFlow(false)
    val isColorBlindMode: StateFlow<Boolean> = _isColorBlindMode.asStateFlow()

    init {
        // Inicializar con datos de prueba
        _products.value = listOf(
            Product(id = 1, name = "Manzana", store = "Walmart", price = "$45.00", previousPrice = "$40.00", trend = Trend.UP),
            Product(id = 2, name = "Plátano", store = "Bodega Aurrerá", price = "$28.00", previousPrice = "$30.00", trend = Trend.DOWN, isFavorite = true),
            Product(id = 3, name = "Naranja", store = "Mercado Local", price = "$35.00", previousPrice = "$35.00", trend = Trend.NEUTRAL),
            Product(id = 4, name = "Aguacate", store = "Chedraui", price = "$120.00", previousPrice = "$110.00", trend = Trend.UP, isFavorite = true),
            Product(id = 5, name = "Tomate", store = "Walmart", price = "$38.00", previousPrice = "$45.00", trend = Trend.DOWN),
            Product(id = 6, name = "Lechuga", store = "Mercado Local", price = "$25.00", previousPrice = "$25.00", trend = Trend.NEUTRAL),
            Product(id = 7, name = "Zanahoria", store = "Soriana", price = "$22.00", previousPrice = "$20.00", trend = Trend.UP),
            Product(id = 8, name = "Papa", store = "Bodega Aurrerá", price = "$18.00", previousPrice = "$20.00", trend = Trend.DOWN, isFavorite = true),
            Product(id = 9, name = "Fresa", store = "Soriana", price = "$65.00", previousPrice = "$60.00", trend = Trend.UP, isFavorite = true)
        )
    }

    fun addProduct(name: String, store: String?, price: String) {
        val newProduct = Product(
            name = name,
            store = store,
            price = price,
            previousPrice = price, // Al ser nuevo, no hay cambio previo aún
            trend = Trend.NEUTRAL
        )
        _products.value = _products.value + newProduct
    }

    fun updateProduct(id: Long, name: String, store: String?, price: String) {
        _products.value = _products.value.map {
            if (it.id == id) {
                val oldPriceVal = it.price.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
                val newPriceVal = price.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
                
                val newTrend = when {
                    newPriceVal > oldPriceVal -> Trend.UP
                    newPriceVal < oldPriceVal -> Trend.DOWN
                    else -> Trend.NEUTRAL
                }
                
                it.copy(
                    name = name,
                    store = store,
                    price = price,
                    previousPrice = it.price,
                    trend = newTrend
                )
            } else it
        }
    }

    fun deleteProduct(productId: Long) {
        _products.value = _products.value.filter { it.id != productId }
    }

    fun toggleFavorite(productId: Long) {
        _products.value = _products.value.map {
            if (it.id == productId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }

    fun setColorBlindMode(enabled: Boolean) {
        _isColorBlindMode.value = enabled
    }
}