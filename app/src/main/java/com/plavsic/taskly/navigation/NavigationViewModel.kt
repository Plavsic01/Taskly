package com.plavsic.taskly.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.plavsic.taskly.domain.auth.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel(){

    private var _isLoggedIn = mutableStateOf(false)
    val isLoggedIn = _isLoggedIn

    init {
        isLoggedIn()
    }


    private fun isLoggedIn() = viewModelScope.launch {
        _isLoggedIn.value = authenticationRepository.isLoggedIn()
    }
}

