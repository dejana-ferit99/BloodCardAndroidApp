package com.example.bloodcardapp.ui.main

import com.example.bloodcardapp.model.BloodDonation

interface OnSelectedDonation {
    fun onDonationLongClickSelected(bloodDonation: BloodDonation)
}