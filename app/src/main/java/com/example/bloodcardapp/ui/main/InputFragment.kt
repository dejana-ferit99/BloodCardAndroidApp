package com.example.bloodcardapp.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.FragmentInputBinding
import com.example.bloodcardapp.databinding.FragmentListBinding
import com.example.bloodcardapp.databinding.FragmentUserProfileBinding
import com.example.bloodcardapp.model.BloodDonation
import com.example.bloodcardapp.ui.auth.RegisterFragmentDirections
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class InputFragment : Fragment() {
    private lateinit var binding: FragmentInputBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private lateinit var bloodCardViewModel : BloodCardViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputBinding.inflate(layoutInflater)
        bloodCardViewModel = ViewModelProvider(
            requireActivity(),
            defaultViewModelProviderFactory
        )[BloodCardViewModel::class.java]
        setUpDate()
        binding.saveInputButton.setOnClickListener { saveInput() }
        return binding.root
    }

    private fun setUpDate() {
        val userGender = bloodCardViewModel.getUserByID(firebaseAuth.currentUser!!.uid)?.gender
        val lastDateDonation = bloodCardViewModel.getLastDateDonation(userGender!!)

        if(lastDateDonation != null){
                setNewDate(lastDateDonation)
        }

    }

    private fun setNewDate(lastDateDonation: Long) {
        if (lastDateDonation != null){
            binding.datePicker.minDate = lastDateDonation
        }
    }



    private fun saveInput() {
        val month = binding.datePicker.month + 1
        val date = String.format("${binding.datePicker.dayOfMonth}/${month}/${binding.datePicker.year}")
        val bloodDonation = BloodDonation(date)
        bloodCardViewModel.saveBloodDonation(bloodDonation, firebaseAuth.currentUser!!.uid)
        Toast.makeText(context, "Saving blood donation!", Toast.LENGTH_SHORT)
            .show()
        Handler(Looper.getMainLooper()).postDelayed({
            switchToList()
        }, 2000)
    }

    private fun switchToList() {
        val action = InputFragmentDirections.actionInputFragmentToUserProfileFragment()
        findNavController().navigate(action)
    }

}