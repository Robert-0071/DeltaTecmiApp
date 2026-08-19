package com.example.actividad1aplicacion

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.actividad1aplicacion.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDoRegister.setOnClickListener {
            val email = binding.inputEmailReg.text.toString()
            val password = binding.inputPasswordReg.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val sharedPref = activity?.getSharedPreferences("Users", Context.MODE_PRIVATE)
                with (sharedPref?.edit()) {
                    this?.putString(email, password)
                    this?.apply()
                }
                Toast.makeText(context, getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}