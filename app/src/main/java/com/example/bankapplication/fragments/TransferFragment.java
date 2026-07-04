package com.example.bankapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.bankapplication.databinding.FragmentTransferBinding;

public class TransferFragment extends Fragment {

    private FragmentTransferBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTransferBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}