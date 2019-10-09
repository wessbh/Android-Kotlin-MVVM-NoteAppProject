package com.wassim.noteapp.login.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wassim.noteapp.login.LoginViewModel
import com.wassim.noteapp.model.repository.IUserRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModelFactory(
    private val userRepo: IUserRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return LoginViewModel(userRepo, Dispatchers.Main) as T
    }

}