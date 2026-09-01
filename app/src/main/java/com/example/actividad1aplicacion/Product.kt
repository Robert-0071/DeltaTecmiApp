package com.example.actividad1aplicacion

enum class Trend {
    UP, DOWN, NEUTRAL
}

data class Product(
    val id: Long = System.currentTimeMillis() + (0..1000).random(),
    val name: String,
    val store: String?,
    val price: String,
    val previousPrice: String = "0.00",
    val trend: Trend = Trend.NEUTRAL,
    var isFavorite: Boolean = false
)