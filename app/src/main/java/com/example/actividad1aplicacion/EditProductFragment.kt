package com.example.actividad1aplicacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.actividad1aplicacion.databinding.FragmentEditProductBinding

class EditProductFragment : Fragment() {

    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var productId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getLong("productId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = viewModel.products.value.find { it.id == productId }
        
        if (product != null) {
            binding.inputNameEdit.setText(product.name)
            binding.inputStoreEdit.setText(product.store ?: "")
            binding.inputPriceEdit.setText(product.price.replace("$", ""))

            binding.btnUpdateProduct.setOnClickListener {
                val name = binding.inputNameEdit.text.toString()
                val store = binding.inputStoreEdit.text.toString()
                val price = binding.inputPriceEdit.text.toString()

                if (name.isNotEmpty() && price.isNotEmpty()) {
                    val updatedProduct = product.copy(
                        name = name,
                        store = if (store.isEmpty()) null else store,
                        price = "$$price"
                    )
                    viewModel.updateProduct(updatedProduct)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(context, "Por favor llena los campos obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnBackEdit.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCancelEdit.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}