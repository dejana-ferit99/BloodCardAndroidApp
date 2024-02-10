package com.example.bloodcardapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.bloodcardapp.model.User

interface IUserRepository {
    fun Save(user: User, password: String)
    fun GetAllUsers(): MutableLiveData<MutableList<User>>
    fun Login(email: String, password: String)
    fun GetUserByID(id: String): User?
}