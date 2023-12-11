package com.example.bloodcardapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.FragmentUserProfileBinding
import com.example.bloodcardapp.ui.auth.LoginFragmentDirections
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private lateinit var bloodCardViewModel: BloodCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(layoutInflater)
        binding.listButton.setOnClickListener { switchList() }
        binding.inputButton.setOnClickListener { switchToInput() }
        bloodCardViewModel = ViewModelProvider(
            requireActivity(),
            defaultViewModelProviderFactory
        )[BloodCardViewModel::class.java]
        setUpProfile()
        return binding.root
    }

    private fun setUpProfile() {
        val user = bloodCardViewModel.getUserByID(firebaseAuth.currentUser!!.uid)
        if(user != null){
            binding.nameHolder.text = user.name
            binding.surnameHolder.text = user.surname
            binding.dateOfBirthHolder.text = user.dateOfBirth
            binding.bloodTypeHolder.text = user.bloodType
            binding.genderHolder.text = user.gender
        }
    }

    private fun switchToInput() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToInputFragment()
        findNavController().navigate(action)
    }

    private fun switchList() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToListFragment()
        findNavController().navigate(action)
    }


}