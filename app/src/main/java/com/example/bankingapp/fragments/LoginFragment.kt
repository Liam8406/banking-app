package com.example.bankingapp.fragments

import android.widget.Toast
import com.example.bankingapp.repository.FirebaseRepo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bankingapp.databinding.FragmentLoginBinding
import androidx.navigation.fragment.findNavController
import com.example.bankingapp.R

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val repository = FirebaseRepo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Login button
        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            repository.login(
                email,
                password,
                onSuccess = {

                    Toast.makeText(
                        requireContext(),
                        "Login successful",
                        Toast.LENGTH_SHORT
                    ).show()

                },
                onFailure = {

                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

                }
            )
        }

        // Go to Register screen
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}