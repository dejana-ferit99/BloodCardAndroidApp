package com.example.bloodcardapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bloodcardapp.model.BloodDonation
import com.example.bloodcardapp.model.User
import com.example.bloodcardapp.repository.BloodDonationRepository
import com.example.bloodcardapp.repository.UserRepository

class BloodCardViewModel(
    private val userRepository: UserRepository,
    private val bloodDonationRepository: BloodDonationRepository
) : ViewModel() {
    var users = userRepository.GetAllUsers()
    var bloodDonations = bloodDonationRepository.GetAllBloodDonations()
    fun save(user: User, password: String){
        val user = userRepository.Save(user, password)
        users = userRepository.GetAllUsers()
        return user
    }
    fun login(email:String, password:String){
        userRepository.Login(email, password)
    }

    fun getUserByID(id: String): User? {
        return userRepository.GetUserByID(id)
    }

    fun saveBloodDonation(bloodDonation: BloodDonation, userID: String){
        bloodDonationRepository.Save(bloodDonation, userID)
        bloodDonations = bloodDonationRepository.GetAllBloodDonations()
    }

    fun getLastDateDonation(gender: String): Long?{
        return bloodDonationRepository.GetLastDateDonation(gender)
    }

    fun deleteBloodDonation(bloodDonation: BloodDonation){
        bloodDonationRepository.Delete(bloodDonation)
        bloodDonations = bloodDonationRepository.GetAllBloodDonations()
    }
}