package com.example.actividad1aplicacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.actividad1aplicacion.databinding.FragmentSecondBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: ProductAdapter
    private var isShowingFavorites = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_SettingsFragment)
        }

        binding.btnAddContainer.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_AddProductFragment)
        }

        binding.btnFavs.setOnClickListener {
            isShowingFavorites = true
            updateTabUI()
            refreshList()
        }

        binding.btnHome.setOnClickListener {
            isShowingFavorites = false
            updateTabUI()
            refreshList()
        }
    }

    private fun updateTabUI() {
        val goldColor = resources.getColor(R.color.text_gold, null)
        val grayColor = android.graphics.Color.parseColor("#80FFFFFF")

        if (isShowingFavorites) {
            binding.titleMain.text = getString(R.string.label_favorites)
            
            binding.imgFavNav.setImageResource(R.drawable.ic_star_filled)
            binding.txtFavNav.setTextColor(goldColor)
            
            binding.imgHomeNav.imageTintList = android.content.res.ColorStateList.valueOf(grayColor)
            binding.txtHomeNav.setTextColor(grayColor)
        } else {
            binding.titleMain.text = getString(R.string.title_delta_tecmi)
            
            binding.imgHomeNav.imageTintList = null
            binding.txtHomeNav.setTextColor(goldColor)
            
            binding.imgFavNav.setImageResource(R.drawable.ic_star_outline)
            binding.txtFavNav.setTextColor(grayColor)
        }
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            products = emptyList(),
            onFavoriteClick = { id -> viewModel.toggleFavorite(id) },
            onMoreClick = { id -> 
                val bundle = Bundle().apply { putLong("productId", id) }
                findNavController().navigate(R.id.action_SecondFragment_to_EditProductFragment, bundle)
            }
        )
        binding.rvProducts.layoutManager = LinearLayoutManager(context)
        binding.rvProducts.adapter = adapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { products ->
                refreshList(products)
            }
        }
    }

    private fun refreshList(allProducts: List<Product> = viewModel.products.value) {
        val filteredList = if (isShowingFavorites) {
            allProducts.filter { it.isFavorite }
        } else {
            allProducts
        }
        adapter.updateData(filteredList)
        binding.subtitleMain.text = "${filteredList.size} artículos"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}