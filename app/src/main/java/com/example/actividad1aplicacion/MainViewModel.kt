package com.example.actividad1aplicacion

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val productsCollection = db.collection("products")

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isColorBlindMode = MutableStateFlow(false)
    val isColorBlindMode: StateFlow<Boolean> = _isColorBlindMode.asStateFlow()

    init {
        // Inicializar con datos de prueba (Los de tu Git)
        seedInitialProducts()
        listenToProducts()
    }

    private fun seedInitialProducts() {
        // Solo para que no aparezca vacía al inicio
        val initialData = listOf(
            Product(id = "1", name = "Manzana", store = "Walmart", price = "$45.00", previousPrice = "$40.00", trend = Trend.UP),
            Product(id = "2", name = "Plátano", store = "Bodega Aurrerá", price = "$28.00", previousPrice = "$30.00", trend = Trend.DOWN, isFavorite = true),
            Product(id = "3", name = "Naranja", store = "Mercado Local", price = "$35.00", previousPrice = "$35.00", trend = Trend.NEUTRAL),
            Product(id = "4", name = "Aguacate", store = "Chedraui", price = "$120.00", previousPrice = "$110.00", trend = Trend.UP, isFavorite = true),
            Product(id = "5", name = "Tomate", store = "Walmart", price = "$38.00", previousPrice = "$45.00", trend = Trend.DOWN),
            Product(id = "6", name = "Lechuga", store = "Mercado Local", price = "$25.00", previousPrice = "$25.00", trend = Trend.NEUTRAL),
            Product(id = "7", name = "Zanahoria", store = "Soriana", price = "$22.00", previousPrice = "$20.00", trend = Trend.UP),
            Product(id = "8", name = "Papa", store = "Bodega Aurrerá", price = "$18.00", previousPrice = "$20.00", trend = Trend.DOWN, isFavorite = true),
            Product(id = "9", name = "Limón", store = "Mercado Local", price = "$55.00", previousPrice = "$50.00", trend = Trend.UP)
        )
        _products.value = initialData
    }

    private fun listenToProducts() {
        productsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            if (snapshot != null && !snapshot.isEmpty) {
                val productList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }
                _products.value = productList
            }
        }
    }

    fun addProduct(name: String, store: String?, price: String) {
        val newProduct = hashMapOf(
            "name" to name,
            "store" to store,
            "price" to price,
            "previousPrice" to price,
            "trend" to Trend.NEUTRAL.name,
            "isFavorite" to false
        )
        productsCollection.add(newProduct)
    }

    fun updateProduct(id: String, name: String, store: String?, price: String) {
        val product = _products.value.find { it.id == id } ?: return
        
        val oldPriceVal = product.price.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
        val newPriceVal = price.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
        
        val newTrend = when {
            newPriceVal > oldPriceVal -> Trend.UP
            newPriceVal < oldPriceVal -> Trend.DOWN
            else -> product.trend
        }

        val updates = hashMapOf(
            "name" to name,
            "store" to store,
            "price" to price,
            "previousPrice" to product.price,
            "trend" to newTrend.name
        )
        productsCollection.document(id).update(updates as Map<String, Any>)
    }

    fun deleteProduct(productId: String) {
        productsCollection.document(productId).delete()
    }

    fun toggleFavorite(productId: String) {
        val product = _products.value.find { it.id == productId } ?: return
        productsCollection.document(productId).update("isFavorite", !product.isFavorite)
    }

    fun setColorBlindMode(enabled: Boolean) {
        _isColorBlindMode.value = enabled
    }
}
