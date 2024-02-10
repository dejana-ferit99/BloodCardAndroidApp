package com.example.bloodcardapp.repository

import androidx.lifecycle.MutableLiveData
import com.example.bloodcardapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository: IUserRepository{
    private var firebaseAuth : FirebaseAuth = Firebase.auth
    private var firebaseStore: FirebaseFirestore = Firebase.firestore
    private var userCollection: CollectionReference = firebaseStore.collection("Users")
    private var usersLiveData: MutableLiveData<MutableList<User>> = MutableLiveData<MutableList<User>>()

    override fun Save(user: User, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener {
                task->
                if (task.isSuccessful){
                    user.id = firebaseAuth.currentUser!!.uid
                    val document: DocumentReference = userCollection.document(user.id)
                    val newUser: MutableMap<String, Any> = HashMap()

                    newUser["id"] = user.id
                    newUser["name"] = user.name
                    newUser["surname"] = user.surname
                    newUser["dateOfBirth"] = user.dateOfBirth
                    newUser["bloodType"] = user.bloodType
                    newUser["gender"] = user.gender
                    newUser["email"] = user.email

                    document.set(newUser)
                }
            }

    }

    override fun GetAllUsers(): MutableLiveData<MutableList<User>> {
        userCollection.get().addOnCompleteListener {
            task ->
            task.addOnSuccessListener {

                usersFromDatabase ->
                if (usersFromDatabase != null && !usersFromDatabase.isEmpty){
                    val users = mutableListOf<User>()
                    for (user in usersFromDatabase){
                        users.add(
                            User(
                                user.data["id"] as String,
                                user.data["name"] as String,
                                user.data["surname"] as String,
                                user.data["dateOfBirth"] as String,
                                user.data["bloodType"] as String,
                                user.data["gender"] as String,
                                user.data["email"] as String
                            )
                        )
                    }
                    usersLiveData.postValue(users)
                }
            }

        }
        return usersLiveData
    }

    override fun Login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            task->task.isSuccessful
        }
    }

    override fun GetUserByID(id: String): User? {
        usersLiveData.value?.forEach {
            user ->
            if (user.id == id){
                return user
            }
        }
        return null
    }
}