package com.wassim.noteapp.model.repository

import com.wassim.noteapp.model.User
import com.wassim.noteapp.common.Result

interface IUserRepository {
    suspend fun getCurrentUser(): Result<Exception, User?>

    suspend fun signOutCurrentUser(): Result<Exception, Unit>

    suspend fun signInGoogleUser(idToken: String): Result<Exception, Unit>
}