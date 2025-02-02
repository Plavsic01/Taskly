package com.plavsic.taskly.ui.registerScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.auth.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    fun createUserDocument(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ){
        authenticationRepository.createUserDocument(
            onSuccess,
            onFailure
        )
    }

}