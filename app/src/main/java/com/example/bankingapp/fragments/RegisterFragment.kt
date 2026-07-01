package com.example.bankingapp.fragments

import android.widget.Toast
import com.example.bankingapp.repository.FirebaseRepo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bankingapp.R
import com.example.bankingapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val repository = FirebaseRepo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnRegister.setOnClickListener {

            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (name.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty()) {

                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            repository.register(
                email,
                password,
                onSuccess = {

                    Toast.makeText(
                        requireContext(),
                        "Account created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    findNavController().navigate(
                        R.id.action_registerFragment_to_loginFragment
                    )
                },
                onFailure = {

                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}