package com.example.actividad1aplicacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1aplicacion.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private val onFavoriteClick: (Long) -> Unit,
    private val onMoreClick: (Long) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.binding.apply {
            txtProductName.text = product.name
            txtStoreName.text = product.store ?: ""
            txtStoreName.visibility = if (product.store == null) View.GONE else View.VISIBLE
            txtProductPrice.text = product.price

            // Tendencia
            val trendIcon = when (product.trend) {
                Trend.UP -> R.drawable.ic_trend_up
                Trend.DOWN -> R.drawable.ic_trend_down
                Trend.NEUTRAL -> R.drawable.ic_trend_neutral
            }
            imgTrend.setImageResource(trendIcon)

            // Favorito
            updateFavoriteIcon(this, product.isFavorite)

            btnFavorite.setOnClickListener {
                onFavoriteClick(product.id)
            }

            btnMore.setOnClickListener {
                onMoreClick(product.id)
            }
        }
    }

    private fun updateFavoriteIcon(binding: ItemProductBinding, isFavorite: Boolean) {
        val starIcon = if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        binding.btnFavorite.setImageResource(starIcon)
    }

    override fun getItemCount() = products.size
}