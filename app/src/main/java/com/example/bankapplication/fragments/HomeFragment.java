package com.example.bankapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
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

        if (auth.getCurrentUser() == null) {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.loginFragment);
            return binding.getRoot();
        }

        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {

                    if (binding == null) return;

                    if (!doc.exists()) {
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    User user = doc.toObject(User.class);
                    if (user == null) return;

                    binding.titleTv.setText("Hello " + user.getName());
                    binding.balanceTv.setText("₪" + user.getBalance());
                })
                .addOnFailureListener(e -> {
                    if (binding == null || getContext() == null) return;
                    Toast.makeText(getContext(), "Failed to load user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.logoutBtn.setOnClickListener(v -> {
            auth.signOut();

            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, true)
                    .build();

            Navigation.findNavController(v).navigate(R.id.loginFragment, null, navOptions);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}