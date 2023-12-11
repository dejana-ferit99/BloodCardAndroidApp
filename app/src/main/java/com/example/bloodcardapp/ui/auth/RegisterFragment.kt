package com.example.bloodcardapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.FragmentRegisterBinding
import com.example.bloodcardapp.model.User
import com.example.bloodcardapp.ui.MainActivity
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var bloodCardViewModel: BloodCardViewModel
    private var firebaseAuth: FirebaseAuth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        binding.returnButton.setOnClickListener { switchToLogin() }
        binding.registerButton.setOnClickListener { register() }
        bloodCardViewModel = ViewModelProvider(
            requireActivity(),
            defaultViewModelProviderFactory
        )[BloodCardViewModel::class.java]
        return binding.root
    }

    private fun register() {

        val name = binding.name.text.toString()
        val surname = binding.surname.text.toString()
        val dateOfBirth = String.format("${binding.dateOfBirth.dayOfMonth}/${binding.dateOfBirth.month}/${binding.dateOfBirth.year}")
        val bloodType = binding.bloodType.text.toString()
        val gender =
            if (binding.genderPicker.checkedRadioButtonId == binding.genderMale.id)
                binding.genderMale.text.toString()
            else
                binding.genderFemale.text.toString()
        val email = binding.email.text.toString()
        val password = binding.passwordET.text.toString()

        val user: User = User("", name, surname, dateOfBirth, bloodType, gender, email)
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname)  || TextUtils.isEmpty(dateOfBirth) || TextUtils.isEmpty(bloodType)
            || TextUtils.isEmpty(gender) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || password.length < 6) {

            Toast.makeText(context, "ERROR: Check your input!", Toast.LENGTH_SHORT).show()
        } else {
            bloodCardViewModel.save(user, password)
            Handler(Looper.getMainLooper()).postDelayed({
                if (firebaseAuth.currentUser != null) {
                    Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(
                        Intent(
                            context,
                            MainActivity::class.java
                        )
                    )
                } else
                    Toast.makeText(context, "Error! Registration unsuccessful!", Toast.LENGTH_LONG)
                        .show()
            }, 3000)
        }
    }

    private fun switchToLogin() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }

}