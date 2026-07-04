package com.example.bankapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bankapplication.databinding.FragmentAdminBinding;
import com.example.bankapplication.models.User;
import com.example.bankapplication.models.UserAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminFragment extends Fragment {

    private FragmentAdminBinding binding;
    private FirebaseFirestore db;
    private ArrayList<User> list;
    private UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAdminBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new UserAdapter(list);

        binding.usersRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.usersRv.setAdapter(adapter);

        db.collection("users")
                .addSnapshotListener((value, error) -> {

                    if (value == null) return;

                    list.clear();

                    value.getDocuments().forEach(doc -> {
                        User u = doc.toObject(User.class);
                        if (u != null) list.add(u);
                    });

                    adapter.notifyDataSetChanged();
                });

        return binding.getRoot();
    }
}