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
            Product(id = 1, name = "Manzana", store = null, price = "$45.00", trend = Trend.NEUTRAL),
            Product(id = 2, name = "Plátano", store = "Bodega Aurrerá", price = "$28.00", trend = Trend.DOWN, isFavorite = true),
            Product(id = 3, name = "Naranja", store = "Mercado Local", price = "$35.00", trend = Trend.NEUTRAL),
            Product(id = 4, name = "Aguacate", store = "Chedraui", price = "$120.00", trend = Trend.UP, isFavorite = true),
            Product(id = 5, name = "Tomate", store = "Walmart", price = "$38.00", trend = Trend.DOWN),
            Product(id = 6, name = "Lechuga", store = "Mercado Local", price = "$25.00", trend = Trend.NEUTRAL),
            Product(id = 7, name = "Zanahoria", store = "Soriana", price = "$22.00", trend = Trend.UP),
            Product(id = 8, name = "Papa", store = "Bodega Aurrerá", price = "$18.00", trend = Trend.DOWN, isFavorite = true),
            Product(id = 9, name = "Fresa", store = "Soriana", price = "$65.00", trend = Trend.UP, isFavorite = true)
        )
    }

    fun addProduct(product: Product) {
        _products.value = _products.value + product
    }

    fun updateProduct(updatedProduct: Product) {
        _products.value = _products.value.map {
            if (it.id == updatedProduct.id) updatedProduct else it
        }
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