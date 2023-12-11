package com.example.bloodcardapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.ActivityAuthBinding
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private val bloodCardViewModel by viewModel<BloodCardViewModel>()
    private var firebaseAuth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        bloodCardViewModel.users.isInitialized
        checkIfLoggedIn()
        setContentView(binding.root)
    }

    private fun checkIfLoggedIn() {
        if (firebaseAuth.currentUser != null) {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }
    }
}