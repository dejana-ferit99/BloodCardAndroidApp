package com.example.bloodcardapp.di

import com.example.bloodcardapp.repository.BloodDonationRepository
import com.example.bloodcardapp.repository.UserRepository
import com.example.bloodcardapp.viewmodel.BloodCardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {

    single {
        UserRepository()
    }
    single {
        BloodDonationRepository()
    }

}

val viewModelModule = module{
    viewModel {
        BloodCardViewModel(get(), get())
    }
}