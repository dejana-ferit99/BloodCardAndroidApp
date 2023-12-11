package com.example.bloodcardapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.FragmentEditBinding
import com.example.bloodcardapp.databinding.FragmentInputBinding
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private lateinit var bloodCardViewModel : BloodCardViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(layoutInflater)
        binding.editSaveButton.setOnClickListener { saveEdit() }
        bloodCardViewModel = ViewModelProvider(
            requireActivity(),
            defaultViewModelProviderFactory
        )[BloodCardViewModel::class.java]
        return binding.root
    }

    private fun saveEdit() {
        val action = EditFragmentDirections.actionEditFragmentToUserProfileFragment()
        findNavController().navigate(action)
    }
    
}