package com.example.bloodcardapp.ui.main

import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodcardapp.R
import com.example.bloodcardapp.databinding.RecyclerViewRowBinding
import com.example.bloodcardapp.model.BloodDonation

class RecyclerAdapter: RecyclerView.Adapter<BloodDonationHolder>(){

    val donationsList = mutableListOf<BloodDonation>()
    var onSelectedDonationListener: OnSelectedDonation?=null

    fun setDonations(donations: MutableList<BloodDonation>){
        donationsList.clear()
        donationsList.addAll(donations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodDonationHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_row, parent, false)
        return BloodDonationHolder(view)
    }

    override fun getItemCount(): Int = donationsList.count()

    override fun onBindViewHolder(holder: BloodDonationHolder, position: Int) {
        val donation = donationsList[position]
        holder.bind(donation)
        onSelectedDonationListener?.let {
        onSelectedDonation ->
                holder.itemView.setOnLongClickListener {
                onSelectedDonation.onDonationLongClickSelected(donation)
                    true
            }
        }
    }

}

class BloodDonationHolder(cellView: View): RecyclerView.ViewHolder(cellView){
    fun bind(bloodDonation: BloodDonation){
        val binding = RecyclerViewRowBinding.bind(itemView)
        binding.donationData.text = bloodDonation.dateOfDonation
    }
}