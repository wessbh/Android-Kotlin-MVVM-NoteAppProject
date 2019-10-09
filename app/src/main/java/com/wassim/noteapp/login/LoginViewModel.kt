package com.wassim.noteapp.login

import androidx.lifecycle.MutableLiveData
import com.wassim.noteapp.common.*
import com.wassim.noteapp.model.LoginResult
import com.wassim.noteapp.model.User
import com.wassim.noteapp.model.repository.IUserRepository
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginViewModel(
    val repo: IUserRepository,
    uiContext: CoroutineContext
) : BaseViewModel<LoginEvent<LoginResult>>(uiContext) {

    //The actual data model is kept private to avoid unwanted tampering
    private val userState = MutableLiveData<User>()

    //Control Logic
    internal val authAttempt = MutableLiveData<Unit>()
    internal val startAnimation = MutableLiveData<Unit>()

    //UI Binding
    internal val signInStatusText = MutableLiveData<String>()
    internal val authButtonText = MutableLiveData<String>()
    internal val satelliteDrawable = MutableLiveData<String>()

    private fun showErrorState() {
        signInStatusText.value = LOGIN_ERROR
        authButtonText.value = SIGN_IN
    }

    private fun showLoadingState() {
        signInStatusText.value = LOADING
        startAnimation.value = Unit
    }

    private fun showSignedInState() {
        signInStatusText.value = SIGNED_IN
        authButtonText.value = SIGN_OUT
    }

    private fun showSignedOutState() {
        signInStatusText.value = SIGNED_OUT
        authButtonText.value = SIGN_IN
    }

    override fun handleEvent(event: LoginEvent<LoginResult>) {
        //Trigger loading screen first
        showLoadingState()
        when (event) {
            is LoginEvent.OnStart -> getUser()
            is LoginEvent.OnAuthButtonClick -> onAuthButtonClick()
            is LoginEvent.OnGoogleSignInResult -> onSignInResult(event.result)
        }
    }

    private fun getUser() = launch {
        val result = repo.getCurrentUser()
        when (result) {
            is Result.Value -> {
                userState.value = result.value
                if (result.value == null) showSignedOutState()
                else showSignedInState()
            }
            is Result.Error -> showErrorState()
        }
    }

    private fun onAuthButtonClick() {
        if (userState.value == null) authAttempt.value = Unit
        else signOutUser()
    }

    private fun onSignInResult(result: LoginResult) = launch {
        if (result.requestCode == RC_SIGN_IN && result.userToken != null) {

            val createGoogleUserResult = repo.signInGoogleUser(
                result.userToken
            )

            //Result.Value means it was successful
            if (createGoogleUserResult is Result.Value) getUser()
            else showErrorState()
        } else {
            showErrorState()
        }
    }

    private fun signOutUser() = launch {
        val result = repo.signOutCurrentUser()

        when (result) {
            is Result.Value -> {
                userState.value = null
                showSignedOutState()
            }
            is Result.Error -> showErrorState()
        }
    }


}