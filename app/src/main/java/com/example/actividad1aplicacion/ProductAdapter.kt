package com.example.actividad1aplicacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.actividad1aplicacion.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private var isColorBlind: Boolean = false,
    private val onFavoriteClick: (String) -> Unit,
    private val onEditClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val expandedIds = mutableSetOf<String>()

    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateData(newProducts: List<Product>, colorBlind: Boolean) {
        products = newProducts
        isColorBlind = colorBlind
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

            // Lógica de Precios y Variación
            val currentVal = product.price.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
            val previousVal = product.previousPrice.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0
            val diff = currentVal - previousVal
            
            txtPreviousPrice.text = "${product.previousPrice} MXN"
            if (previousVal > 0) {
                val percentage = (diff / previousVal) * 100
                txtVariation.text = String.format("%.2f (%.1f%%)", diff, percentage)
            } else {
                txtVariation.text = "Sin cambios"
            }

            // Tendencia
            val trendIcon = when (product.trend) {
                Trend.UP -> R.drawable.ic_trend_up
                Trend.DOWN -> R.drawable.ic_trend_down
                Trend.NEUTRAL -> R.drawable.ic_trend_neutral
            }
            imgTrend.setImageResource(trendIcon)

            // Colores de tendencia
            val trendColorRes = when (product.trend) {
                Trend.UP -> if (isColorBlind) R.color.trend_up_daltonism else R.color.trend_up_normal
                Trend.DOWN -> if (isColorBlind) R.color.trend_down_daltonism else R.color.trend_down_normal
                Trend.NEUTRAL -> R.color.trend_neutral
            }
            val resolvedColor = holder.itemView.context.getColor(trendColorRes)
            imgTrend.imageTintList = android.content.res.ColorStateList.valueOf(resolvedColor)
            txtVariation.setTextColor(resolvedColor)

            // Expansión
            val isExpanded = expandedIds.contains(product.id)
            variationSection.visibility = if (isExpanded) View.VISIBLE else View.GONE
            btnExpand.rotation = if (isExpanded) 180f else 0f

            btnExpand.setOnClickListener {
                if (isExpanded) expandedIds.remove(product.id) else expandedIds.add(product.id)
                notifyItemChanged(position)
            }

            // Favorito
            updateFavoriteIcon(this, product.isFavorite)
            btnFavorite.setOnClickListener { onFavoriteClick(product.id) }

            // Menús
            btnMore.setOnClickListener { view -> showPopupMenu(view, product.id) }
        }
    }

    private fun showPopupMenu(view: View, productId: String) {
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.menu_product_item)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> { onEditClick(productId); true }
                R.id.action_delete -> { onDeleteClick(productId); true }
                else -> false
            }
        }
        popup.show()
    }

    private fun updateFavoriteIcon(binding: ItemProductBinding, isFavorite: Boolean) {
        val starIcon = if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
        binding.btnFavorite.setImageResource(starIcon)
    }

    override fun getItemCount() = products.size
}
