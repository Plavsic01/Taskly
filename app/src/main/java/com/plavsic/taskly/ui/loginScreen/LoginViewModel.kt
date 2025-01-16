package com.plavsic.taskly.ui.loginScreen

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.AuthResult
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel(){

    private val _loginFlow = MutableSharedFlow<Response<AuthResult>>()
    val loginFlow = _loginFlow

    fun login(email: String, password: String) = viewModelScope.launch {
        authenticationRepository.login(email, password).collect { response ->
            _loginFlow.emit(response)
        }
    }

    fun loginWithGoogle(credential: Credential) = viewModelScope.launch {
        if(credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)
            authenticationRepository.loginWithGoogle(googleIdTokenCredential.idToken).collect { response ->
                _loginFlow.emit(response)
            }
        }else{
            Log.e("Error_google_login","UNEXPECTED_CREDENTIAL")
        }
    }
}