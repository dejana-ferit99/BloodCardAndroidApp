package com.example.bloodcardapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.FragmentListBinding
import com.example.bloodcardapp.databinding.FragmentUserProfileBinding
import com.example.bloodcardapp.model.BloodDonation
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ListFragment : Fragment(), OnSelectedDonation {
    private lateinit var binding: FragmentListBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private lateinit var bloodCardViewModel : BloodCardViewModel
    private lateinit var adapter: RecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)
        bloodCardViewModel = ViewModelProvider(
            requireActivity(),
            defaultViewModelProviderFactory
        )[BloodCardViewModel::class.java]
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.bloodDonationList.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        adapter = RecyclerAdapter()
        bloodCardViewModel.bloodDonations.observe(viewLifecycleOwner){
            donations ->
            if(!donations.isNullOrEmpty()){
                adapter.setDonations(donations)
            }
        }
        adapter.onSelectedDonationListener = this
        binding.bloodDonationList.adapter = adapter
    }

    override fun onDonationLongClickSelected(bloodDonation: BloodDonation) {
        bloodCardViewModel.deleteBloodDonation(bloodDonation)
    }

}