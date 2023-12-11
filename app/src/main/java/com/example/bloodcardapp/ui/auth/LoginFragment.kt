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
import com.example.bloodcardapp.databinding.FragmentLoginBinding
import com.example.bloodcardapp.ui.MainActivity
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Text

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private lateinit var bloodCardViewModel: BloodCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.nextButton.setOnClickListener { switchToRegister() }
        binding.loginButton.setOnClickListener { login() }
        bloodCardViewModel = ViewModelProvider(
            requireActivity(),
            defaultViewModelProviderFactory
        )[BloodCardViewModel::class.java]
        return binding.root
    }

    private fun login() {
        val email = binding.email.text.toString()
        val password = binding.passwordET.text.toString()
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(context, "ERROR: Check your input!", Toast.LENGTH_SHORT).show()
        } else {
            bloodCardViewModel.login(email, password)
            Handler(Looper.getMainLooper()).postDelayed({
                if (firebaseAuth.currentUser != null) {
                    Toast.makeText(context, "Logged in successfully!", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(
                        Intent(
                            context,
                            MainActivity::class.java
                        )
                    )
                } else
                    Toast.makeText(context, "Error! Login unsuccessful!", Toast.LENGTH_LONG)
                        .show()
            }, 2000)
        }
    }

    private fun switchToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }
}