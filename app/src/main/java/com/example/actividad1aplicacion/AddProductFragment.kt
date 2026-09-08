package com.example.actividad1aplicacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.actividad1aplicacion.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackAdd.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCancelAdd.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSaveProduct.setOnClickListener {
            val name = binding.inputName.text.toString()
            val store = binding.inputStore.text.toString()
            val price = binding.inputPrice.text.toString()

            binding.errorName.visibility = if (name.isEmpty()) View.VISIBLE else View.GONE
            binding.errorPrice.visibility = if (price.isEmpty()) View.VISIBLE else View.GONE

            if (name.isNotEmpty() && price.isNotEmpty()) {
                viewModel.addProduct(
                    name = name,
                    store = if (store.isEmpty()) null else store,
                    price = "$$price"
                )
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
