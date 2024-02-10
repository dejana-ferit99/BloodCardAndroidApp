package com.example.bloodcardapp.ui.main

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.FragmentUserProfileBinding
import com.example.bloodcardapp.model.User
import com.example.bloodcardapp.ui.auth.LoginFragmentDirections
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private var firebaseAuth: FirebaseAuth = Firebase.auth
    private lateinit var bloodCardViewModel: BloodCardViewModel
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private var lastBloodDonationDate: Long? = null
    var flag = true

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
        checkIfAbleToDonate()
        binding.locationButton.setOnClickListener {
            val link = Uri.parse("https://www.hck.hr/kako-pomoci/darujte-krv/kalendar-akcija-dobrovoljnog-davanja-krvi/11581")
            val intent = Intent(Intent.ACTION_VIEW, link)
            startActivity(intent)
        }
        return binding.root
    }

    private fun checkIfAbleToDonate() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH) + 1
        val day = currentDate.get(Calendar.DAY_OF_MONTH)
        val dateInString = String.format("$day/$month/$year")
        val currentDateConverted = convertDateToLong(dateInString)

        if(flag == true){
            if (lastBloodDonationDate == null || currentDateConverted >= lastBloodDonationDate!!) {
                notificationManager = activity?.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager
                val notificationIntent = Intent()
                val pendingIntent = PendingIntent.getActivity(activity, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)
                notificationChannel = NotificationChannel("i.apps.notifications", "Donation possible", NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)
                builder = Notification.Builder(context, "i.apps.notifications")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Donation is possible!")
                    .setContentIntent(pendingIntent)
                notificationManager.notify(1, builder.build())
            } else {
                Toast.makeText(context, "You can not make blood donation yet!", Toast.LENGTH_SHORT).show()
            }
            flag = false
        }
    }

    private fun setUpProfile() {
        val user = bloodCardViewModel.getUserByID(firebaseAuth.currentUser!!.uid)
        if(user != null){
            binding.nameHolder.text = user.name
            binding.surnameHolder.text = user.surname
            binding.dateOfBirthHolder.text = user.dateOfBirth
            binding.bloodTypeHolder.text = user.bloodType
            binding.genderHolder.text = user.gender

            findLastDonation(user)
        }

    }

    private fun findLastDonation(user: User) {
        lastBloodDonationDate = bloodCardViewModel.getLastDateDonation(user.gender)
    }

    private fun switchToInput() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToInputFragment()
        findNavController().navigate(action)
    }

    private fun switchList() {
        val action = UserProfileFragmentDirections.actionUserProfileFragmentToListFragment()
        findNavController().navigate(action)
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("dd/MM/yyyy")
        return df.parse(date)?.time ?: System.currentTimeMillis()
    }


}