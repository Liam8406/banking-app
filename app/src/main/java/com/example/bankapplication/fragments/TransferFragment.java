package com.example.bankapplication.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bankapplication.databinding.FragmentTransferBinding;
import com.example.bankapplication.models.Transaction;
import com.example.bankapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TransferFragment extends Fragment {

    private FragmentTransferBinding binding;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTransferBinding.inflate(inflater, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.transferBtn.setOnClickListener(v -> transferMoney());

        return binding.getRoot();
    }

    private void transferMoney() {

        String receiverId = binding.toAccountEt.getText().toString().trim();
        String amountString = binding.amountEt.getText().toString().trim();

        if (TextUtils.isEmpty(receiverId) || TextUtils.isEmpty(amountString)) {
            showMessage("Fill all fields", Color.RED);
            return;
        }

        double amount = Double.parseDouble(amountString);

        if (amount <= 0) {
            showMessage("Invalid amount", Color.RED);
            return;
        }

        String currentUid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(currentUid)
                .get()
                .addOnSuccessListener(senderSnapshot -> {

                    if (!senderSnapshot.exists()) {
                        showMessage("Sender not found", Color.RED);
                        return;
                    }

                    User sender = senderSnapshot.toObject(User.class);

                    if (sender == null)
                        return;

                    if (sender.getBalance() < amount) {
                        showMessage("Insufficient balance", Color.RED);
                        return;
                    }

                    db.collection("users")
                            .whereEqualTo("id", receiverId)
                            .get()
                            .addOnSuccessListener(query -> {

                                if (query.isEmpty()) {
                                    showMessage("Recipient not found", Color.RED);
                                    return;
                                }

                                DocumentSnapshot receiverDoc = query.getDocuments().get(0);

                                User receiver = receiverDoc.toObject(User.class);

                                if (receiver == null)
                                    return;

                                double newSenderBalance =
                                        sender.getBalance() - amount;

                                double newReceiverBalance =
                                        receiver.getBalance() + amount;

                                db.collection("users")
                                        .document(currentUid)
                                        .update("balance", newSenderBalance);

                                db.collection("users")
                                        .document(receiverDoc.getId())
                                        .update("balance", newReceiverBalance);

                                Transaction transaction =
                                        new Transaction(
                                                sender.getId(),
                                                receiver.getId(),
                                                amount,
                                                System.currentTimeMillis());

                                db.collection("transactions")
                                        .add(transaction)
                                        .addOnSuccessListener(unused -> {

                                            showMessage("Transfer completed",
                                                    Color.parseColor("#16A34A"));

                                            binding.amountEt.setText("");
                                            binding.toAccountEt.setText("");

                                        });

                            });

                });
    }

    private void showMessage(String text, int color) {

        binding.messageTv.setText(text);
        binding.messageTv.setTextColor(color);

    }
}