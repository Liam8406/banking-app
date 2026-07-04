package com.example.bankapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bankapplication.R;
import com.example.bankapplication.databinding.FragmentRegisterBinding;
import com.example.bankapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.registerBtn.setOnClickListener(v -> {

            String name = binding.nameEt.getText().toString().trim();
            String id = binding.idEt.getText().toString().trim();
            String email = binding.emailEt.getText().toString().trim();
            String phone = binding.phoneEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();

            if (name.isEmpty() || id.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                return;
            }

            if (!email.contains("@")) {
                return;
            }

            if (phone.length() < 9) {
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> {

                        if (auth.getCurrentUser() == null) return;

                        String uid = auth.getCurrentUser().getUid();

                        User user = new User(uid, name, email, phone, 1000);

                        db.collection("users")
                                .document(uid)
                                .set(user)
                                .addOnSuccessListener(unused ->
                                        Navigation.findNavController(v).navigate(R.id.toLogin));
                    })
                    .addOnFailureListener(e -> e.printStackTrace());
        });

        binding.goLoginBtn.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.toLogin));

        return binding.getRoot();
    }
}