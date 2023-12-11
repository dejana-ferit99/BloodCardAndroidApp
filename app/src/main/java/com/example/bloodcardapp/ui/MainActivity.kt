package com.example.bloodcardapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.ActivityMainBinding
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val bloodCardViewModel by viewModel<BloodCardViewModel>()
    private var firebaseAuth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.logoutButton.setOnClickListener { logout() }
        bloodCardViewModel.users.isInitialized
        bloodCardViewModel.bloodDonations.isInitialized
        setContentView(binding.root)
    }

    private fun logout() {
        firebaseAuth.signOut()
        startActivity(
            Intent(
                this,
                AuthActivity::class.java
            )
        )
        finish()
    }
}