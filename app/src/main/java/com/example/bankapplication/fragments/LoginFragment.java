package com.example.bankapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bankapplication.databinding.FragmentLoginBinding;
import com.example.bankapplication.R;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.goRegisterBtn.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.toRegister));

        //binding.loginBtn.setOnClickListener(v ->
                //Navigation.findNavController(v).navigate(R.id.homeFragment));

        return binding.getRoot();
    }
}