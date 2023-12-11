package com.example.bloodcardapp.repository

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.example.bloodcardapp.model.BloodDonation
import com.example.bloodcardapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class BloodDonationRepository: IBloodDonationRepository {
    private val looper = Looper.myLooper()
    private var firebaseAuth : FirebaseAuth = Firebase.auth
    private var firebaseStore: FirebaseFirestore = Firebase.firestore
    private lateinit var bloodDonationCollection: CollectionReference
    private var bloodDonationsLiveData: MutableLiveData<MutableList<BloodDonation>> = MutableLiveData<MutableList<BloodDonation>>()

    override fun Save(bloodDonation: BloodDonation, userID: String) {
        bloodDonationCollection = firebaseStore.collection("Users/${userID}/BloodDonations")
        val document: DocumentReference = bloodDonationCollection.document()
        val newBloodDonation: MutableMap<String, Any> = HashMap()

        newBloodDonation["dateOfDonation"] = bloodDonation.dateOfDonation
        document.set(newBloodDonation)
        bloodDonationsLiveData.value?.add(bloodDonation)
    }

    override fun GetAllBloodDonations(): MutableLiveData<MutableList<BloodDonation>> {
        if (firebaseAuth.currentUser != null){
            bloodDonationCollection = firebaseStore.collection("Users/${firebaseAuth.currentUser!!.uid}/BloodDonations")
            bloodDonationCollection.get().addOnSuccessListener {
                    donations ->
                if(donations != null && !donations.isEmpty){
                    val donationList = mutableListOf<BloodDonation>()
                    donations.forEach{
                            donation ->
                        donationList.add(
                            BloodDonation(
                                donation.data["dateOfDonation"] as String
                            )
                        )
                    }
                    bloodDonationsLiveData.postValue(donationList)
                }
            }
        }
        return bloodDonationsLiveData
    }

    override fun GetLastDateDonation(gender: String): Long? {
        val donationList = bloodDonationsLiveData.value
        if (donationList != null && donationList.isNotEmpty()){
            val donationsByDate = mutableListOf<Long>()
            donationList.forEach {
                donation->
                var lastDateDonation = donation.dateOfDonation.split("/")
                var newMonth = lastDateDonation[1].toInt()
                var year = lastDateDonation[2].toInt()
                val day = lastDateDonation[0].toInt()
                if(gender.equals("F")){
                    newMonth+= FEMALE_DATE_IN_MONTHS_GAP
                } else{
                    newMonth+= MALE_DATE_IN_MONTHS_GAP
                }
                if(newMonth > 12){
                    year+=1
                    newMonth-=12
                }
                donationsByDate.add(convertDateToLong(String.format("$day/$newMonth/$year")))
            }
            donationsByDate.sort()
            return donationsByDate.last()
        }
        return null
    }

    override fun Delete(bloodDonation: BloodDonation) {
        var documentReference: DocumentReference
        bloodDonationCollection =
            firebaseStore.collection("Users/${firebaseAuth.currentUser!!.uid}/BloodDonations")
        bloodDonationCollection.get().addOnSuccessListener { donations ->
            if (donations != null && !donations.isEmpty) {
                val donationList = mutableListOf<BloodDonation>()
                donations.forEach { donation ->
                    if (donation.data["dateOfDonation"] as String == bloodDonation.dateOfDonation) {
                        documentReference = bloodDonationCollection.document(donation.id)
                        documentReference.delete()
                    }
                }
            }
        }
        looper?.let {
            Handler(it).postDelayed({
                val updated: MutableList<BloodDonation>? = bloodDonationsLiveData.value
                updated?.remove(bloodDonation)
                if (updated != null) {
                    bloodDonationsLiveData.postValue(updated!!)
                }
            }, 1000)
        }
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd/MM/yyyy")
        return df.parse(date)?.time ?: System.currentTimeMillis()
    }

    companion object{
        val MALE_DATE_IN_MONTHS_GAP = 3
        val FEMALE_DATE_IN_MONTHS_GAP = 4
    }


}