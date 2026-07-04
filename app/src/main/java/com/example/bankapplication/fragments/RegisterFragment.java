package com.example.bankapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bankapplication.databinding.FragmentRegisterBinding;
import com.example.bankapplication.R;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.registerBtn.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.toHome));

        binding.goLoginBtn.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.toLogin));

        return binding.getRoot();
    }
}