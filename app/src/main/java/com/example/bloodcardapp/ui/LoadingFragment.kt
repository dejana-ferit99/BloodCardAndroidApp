package com.example.bloodcardapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.FragmentLoadingBinding
import com.example.bloodcardapp.databinding.FragmentLoginBinding
import com.example.bloodcardapp.databinding.FragmentRegisterBinding
import com.example.bloodcardapp.ui.auth.RegisterFragmentDirections
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoadingFragment : Fragment() {

    private lateinit var binding: FragmentLoadingBinding
    private val bloodCardViewModel by viewModel<BloodCardViewModel>()
    private var firebaseAuth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingBinding.inflate(layoutInflater)
        loading()
        return binding.root
    }

    private fun loading() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                switchToProfile()
            }, 2000
        )
    }

    private fun switchToProfile() {
        val action = LoadingFragmentDirections.actionLoadingFragmentToUserProfileFragment()
        findNavController().navigate(action)
    }
}