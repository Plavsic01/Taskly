package com.plavsic.taskly.ui.registerScreen

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.AuthResult
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.auth.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
):ViewModel() {
    private var _registerFlow = MutableSharedFlow<Response<AuthResult>>()
    val registerFlow = _registerFlow

    fun register(email:String,password:String) {
        viewModelScope.launch {
            authenticationRepository.register(email = email,password = password).collect {
                _registerFlow.emit(it)
            }
        }
    }

    fun registerWithGoogle(credential: Credential) = viewModelScope.launch {
        if(credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)
            authenticationRepository.loginWithGoogle(googleIdTokenCredential.idToken).collect { response ->
                _registerFlow.emit(response)
            }
        }else{
            Log.e("Error_google_register","Error with registration")
        }
    }
}