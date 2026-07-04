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

        if (binding == null) return;

        String receiverId = binding.toAccountEt.getText().toString().trim();
        String amountString = binding.amountEt.getText().toString().trim();

        if (TextUtils.isEmpty(receiverId) || TextUtils.isEmpty(amountString)) {
            showMessage("Fill all fields", Color.RED);
            return;
        }

        // Prevent crash on invalid number input (e.g. "12.34.56", "," instead of ".", etc.)
        double amount;
        try {
            amount = Double.parseDouble(amountString.replace(",", "."));
        } catch (NumberFormatException e) {
            showMessage("Enter a valid amount", Color.RED);
            return;
        }

        if (amount <= 0) {
            showMessage("Invalid amount", Color.RED);
            return;
        }

        if (auth.getCurrentUser() == null) {
            showMessage("You are not logged in", Color.RED);
            return;
        }

        String currentUid = auth.getCurrentUser().getUid();

        // Disable button to prevent double-taps while the transfer is in progress
        binding.transferBtn.setEnabled(false);

        db.collection("users")
                .document(currentUid)
                .get()
                .addOnSuccessListener(senderSnapshot -> {

                    if (binding == null) return;

                    if (!senderSnapshot.exists()) {
                        showMessage("Sender not found", Color.RED);
                        binding.transferBtn.setEnabled(true);
                        return;
                    }

                    User sender = senderSnapshot.toObject(User.class);

                    if (sender == null) {
                        binding.transferBtn.setEnabled(true);
                        return;
                    }

                    if (receiverId.equals(sender.getId())) {
                        showMessage("You cannot transfer to yourself", Color.RED);
                        binding.transferBtn.setEnabled(true);
                        return;
                    }

                    if (sender.getBalance() < amount) {
                        showMessage("Insufficient balance", Color.RED);
                        binding.transferBtn.setEnabled(true);
                        return;
                    }

                    db.collection("users")
                            .whereEqualTo("id", receiverId)
                            .get()
                            .addOnSuccessListener(query -> {

                                if (binding == null) return;

                                if (query.isEmpty()) {
                                    showMessage("Recipient not found", Color.RED);
                                    binding.transferBtn.setEnabled(true);
                                    return;
                                }

                                DocumentSnapshot receiverDoc = query.getDocuments().get(0);

                                User receiver = receiverDoc.toObject(User.class);

                                if (receiver == null) {
                                    binding.transferBtn.setEnabled(true);
                                    return;
                                }

                                double newSenderBalance =
                                        sender.getBalance() - amount;

                                double newReceiverBalance =
                                        receiver.getBalance() + amount;

                                db.collection("users")
                                        .document(currentUid)
                                        .update("balance", newSenderBalance)
                                        .addOnFailureListener(e -> {
                                            if (binding != null) {
                                                showMessage("Transfer failed: " + e.getMessage(), Color.RED);
                                                binding.transferBtn.setEnabled(true);
                                            }
                                        });

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

                                            if (binding == null) return;

                                            showMessage("Transfer completed",
                                                    Color.parseColor("#16A34A"));

                                            binding.amountEt.setText("");
                                            binding.toAccountEt.setText("");
                                            binding.transferBtn.setEnabled(true);

                                        })
                                        .addOnFailureListener(e -> {
                                            if (binding != null) {
                                                binding.transferBtn.setEnabled(true);
                                            }
                                        });

                            })
                            .addOnFailureListener(e -> {
                                if (binding != null) {
                                    showMessage("Error finding recipient: " + e.getMessage(), Color.RED);
                                    binding.transferBtn.setEnabled(true);
                                }
                            });

                })
                .addOnFailureListener(e -> {
                    if (binding != null) {
                        showMessage("Error: " + e.getMessage(), Color.RED);
                        binding.transferBtn.setEnabled(true);
                    }
                });
    }

    private void showMessage(String text, int color) {
        if (binding == null) return;
        binding.messageTv.setText(text);
        binding.messageTv.setTextColor(color);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}