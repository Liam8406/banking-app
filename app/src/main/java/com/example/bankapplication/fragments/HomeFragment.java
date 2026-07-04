package com.example.bankapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bankapplication.R;
import com.example.bankapplication.databinding.FragmentHomeBinding;
import com.example.bankapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {

                    User user = doc.toObject(User.class);

                    if (user == null) return;

                    binding.titleTv.setText("Hello " + user.getName());
                    binding.balanceTv.setText("₪" + user.getBalance());
                });

        binding.logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            Navigation.findNavController(v).navigate(R.id.loginFragment);
        });

        return binding.getRoot();
    }
}