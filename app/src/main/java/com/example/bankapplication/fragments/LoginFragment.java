package com.example.bankapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bankapplication.R;
import com.example.bankapplication.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();

        binding.loginBtn.setOnClickListener(v -> {

            String email = binding.emailEt.getText().toString();
            String password = binding.passwordEt.getText().toString();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result ->
                            Navigation.findNavController(v).navigate(R.id.toHome));
        });

        binding.goRegisterBtn.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.toRegister));

        return binding.getRoot();
    }
}