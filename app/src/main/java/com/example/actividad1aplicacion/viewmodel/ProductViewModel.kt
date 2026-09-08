package com.example.actividad1aplicacion.viewmodel

import androidx.lifecycle.ViewModel
import com.example.actividad1aplicacion.model.Product
import com.example.actividad1aplicacion.model.Trend
import com.example.actividad1aplicacion.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.products

    private val _isColorBlindMode = MutableStateFlow(false)
    val isColorBlindMode: StateFlow<Boolean> = _isColorBlindMode.asStateFlow()

    private val _isTalkBackMode = MutableStateFlow(false)
    val isTalkBackMode: StateFlow<Boolean> = _isTalkBackMode.asStateFlow()

    fun addProduct(name: String, store: String?, price: String) {
        repository.addProduct(name, store, price)
    }

    fun updateProduct(id: String, name: String, store: String?, price: String) {
        val product = products.value.find { it.id == id } ?: return
        
        val oldPriceVal = product.price.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
        val newPriceVal = price.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
        
        val newTrend = when {
            newPriceVal > oldPriceVal -> Trend.UP
            newPriceVal < oldPriceVal -> Trend.DOWN
            else -> product.trend
        }

        repository.updateProduct(id, name, store, price, product.price, newTrend)
    }

    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId)
    }

    fun toggleFavorite(productId: String, currentStatus: Boolean) {
        repository.toggleFavorite(productId, currentStatus)
    }

    fun setColorBlindMode(enabled: Boolean) {
        _isColorBlindMode.value = enabled
    }

    fun setTalkBackMode(enabled: Boolean) {
        _isTalkBackMode.value = enabled
    }
}
