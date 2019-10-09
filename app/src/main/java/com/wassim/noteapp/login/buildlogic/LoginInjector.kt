package com.wassim.noteapp.login.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.FirebaseApp
import com.wassim.noteapp.login.buildlogic.LoginViewModelFactory
import com.wassim.noteapp.model.repository.IUserRepository
import com.wassim.noteapp.model.implementations.FirebaseUserRepoImpl

class LoginInjector(application: Application): AndroidViewModel(application) {

    init {
        FirebaseApp.initializeApp(application)
    }

    private fun getUserRepository(): IUserRepository {
        return FirebaseUserRepoImpl()
    }

    fun provideUserViewModelFactory(): LoginViewModelFactory =
        LoginViewModelFactory(
            getUserRepository()
     )
}