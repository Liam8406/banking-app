package com.example.bankingapp.repository

import com.google.firebase.auth.FirebaseAuth

class FirebaseRepo {

    private val auth = FirebaseAuth.getInstance()

    fun register(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Registration failed")
            }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Login failed")
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUser() = auth.currentUser
}