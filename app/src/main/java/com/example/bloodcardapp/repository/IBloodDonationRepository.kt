package com.example.bloodcardapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.bloodcardapp.model.BloodDonation

interface IBloodDonationRepository {
    fun Save(bloodDonation: BloodDonation, userID: String)
    fun GetAllBloodDonations(): MutableLiveData<MutableList<BloodDonation>>
    fun GetLastDateDonation(gender:String): Long?
    fun Delete(bloodDonation: BloodDonation)
}