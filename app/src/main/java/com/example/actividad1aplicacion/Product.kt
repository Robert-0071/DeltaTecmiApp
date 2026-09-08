package com.example.actividad1aplicacion

enum class Trend {
    UP, DOWN, NEUTRAL
}

data class Product(
    val id: String = "",
    val name: String = "",
    val store: String? = null,
    val price: String = "",
    val previousPrice: String = "0.00",
    val trend: Trend = Trend.NEUTRAL,
    var isFavorite: Boolean = false
)
