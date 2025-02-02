package com.plavsic.taskly.ui.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plavsic.taskly.core.UIState
import com.plavsic.taskly.domain.auth.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _authProvider = MutableStateFlow(Provider.DEFAULT)
    val authProvider = _authProvider.asStateFlow()


    init {
        checkAuthProvider()
    }

    val userInfo = authenticationRepository.getUserInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UIState.Idle
        )


    private fun checkAuthProvider()  {
        for(profile in authenticationRepository.currentUser()!!.providerData){
          when (profile.providerId) {
              "google.com" -> {
                    _authProvider.value = Provider.GOOGLE
              }
              "password" -> {
                  _authProvider.value = Provider.EMAIL
              }
          }
        }
    }

    // ADD TRY CATCH AND OnSuccess and onError for both

    fun updateUsername(username:String) {
        viewModelScope.launch {
            authenticationRepository.updateUsername(username)
        }
    }

    fun updateProfilePicture(uri:String) {
        viewModelScope.launch {
            authenticationRepository.updateProfilePicture(uri)
        }
    }

    fun reauthenticateAndChangePassword(
        currentPassword:String,
        newPassword:String,
        onSuccess:() -> Unit,
        onFailure:(Exception) -> Unit
    ){
        viewModelScope.launch {
            authenticationRepository.reauthenticateAndChangePassword(
                currentPassword,
                newPassword,
                onSuccess,
                onFailure
            )
        }
    }


    fun logOut() {
        authenticationRepository.logout()
    }

}