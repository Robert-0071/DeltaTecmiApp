package com.example.actividad1aplicacion.repository

import com.example.actividad1aplicacion.model.Product
import com.example.actividad1aplicacion.model.Trend
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val productsCollection = db.collection("products")
    
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val staticInitialProducts = listOf(
        Product(id = "1", name = "Manzana", store = "Walmart", price = "$45.00", previousPrice = "$40.00", trend = Trend.UP, isAdded = true),
        Product(id = "2", name = "Plátano", store = "Bodega Aurrerá", price = "$28.00", previousPrice = "$30.00", trend = Trend.DOWN, isFavorite = true, isAdded = true),
        Product(id = "3", name = "Naranja", store = "Mercado Local", price = "$35.00", previousPrice = "$35.00", trend = Trend.NEUTRAL, isAdded = true),
        Product(id = "4", name = "Aguacate", store = "Chedraui", price = "$120.00", previousPrice = "$110.00", trend = Trend.UP, isFavorite = true, isAdded = true),
        Product(id = "5", name = "Tomate", store = "Walmart", price = "$38.00", previousPrice = "$45.00", trend = Trend.DOWN, isAdded = true),
        Product(id = "6", name = "Lechuga", store = "Mercado Local", price = "$25.00", previousPrice = "$25.00", trend = Trend.NEUTRAL, isAdded = true),
        Product(id = "7", name = "Zanahoria", store = "Soriana", price = "$22.00", previousPrice = "$20.00", trend = Trend.UP, isAdded = true),
        Product(id = "8", name = "Papa", store = "Bodega Aurrerá", price = "$18.00", previousPrice = "$20.00", trend = Trend.DOWN, isFavorite = true, isAdded = true),
        Product(id = "9", name = "Limón", store = "Mercado Local", price = "$55.00", previousPrice = "$50.00", trend = Trend.UP, isAdded = true),
        Product(id = "10", name = "Fresa", store = "Walmart", price = "$65.00", previousPrice = "$60.00", trend = Trend.UP, isAdded = true),
        Product(id = "11", name = "Cebolla", store = "Chedraui", price = "$32.00", previousPrice = "$35.00", trend = Trend.DOWN, isAdded = true),
        Product(id = "12", name = "Pepino", store = "Mercado Local", price = "$24.00", previousPrice = "$24.00", trend = Trend.NEUTRAL, isAdded = true)
    )

    init {
        _products.value = staticInitialProducts
        listenToProducts()
    }

    private fun listenToProducts() {
        productsCollection.addSnapshotListener { snapshot, _ ->
            val firebaseList = if (snapshot != null && !snapshot.isEmpty) {
                snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id, isAdded = true)
                }
            } else {
                emptyList()
            }
            // Mantenemos los estáticos + los de Firebase
            _products.value = staticInitialProducts + firebaseList
        }
    }

    fun addProduct(name: String, store: String?, price: String) {
        val newProduct = hashMapOf(
            "name" to name,
            "store" to store,
            "price" to price,
            "previousPrice" to price,
            "trend" to Trend.NEUTRAL.name,
            "isFavorite" to false,
            "isAdded" to true
        )
        productsCollection.add(newProduct)
    }

    fun updateProduct(id: String, name: String, store: String?, price: String, previousPrice: String, trend: Trend) {
        // Solo intentamos actualizar en Firebase si NO es uno de los IDs estáticos (1 al 12)
        if (id.length > 2) { 
            val updates = hashMapOf(
                "name" to name,
                "store" to store,
                "price" to price,
                "previousPrice" to previousPrice,
                "trend" to trend.name
            )
            productsCollection.document(id).update(updates as Map<String, Any>)
        } else {
            // Si es estático, actualizamos la lista local para que veas el cambio inmediatamente
            _products.value = _products.value.map {
                if (it.id == id) it.copy(name = name, store = store, price = price, previousPrice = previousPrice, trend = trend)
                else it
            }
        }
    }

    fun deleteProduct(productId: String) {
        if (productId.length > 2) {
            productsCollection.document(productId).delete()
        } else {
            _products.value = _products.value.filterNot { it.id == productId }
        }
    }

    fun toggleFavorite(productId: String, currentStatus: Boolean) {
        if (productId.length > 2) {
            productsCollection.document(productId).update("isFavorite", !currentStatus)
        } else {
            _products.value = _products.value.map {
                if (it.id == productId) it.copy(isFavorite = !currentStatus)
                else it
            }
        }
    }
}
