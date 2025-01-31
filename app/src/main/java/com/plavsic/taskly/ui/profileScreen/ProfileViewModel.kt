package com.plavsic.taskly.ui.profileScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plavsic.taskly.core.Response
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


    private val _userPicture = MutableStateFlow(getUserProfilePicture())
    val userPicture = _userPicture.asStateFlow()

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

    fun getUserProfilePicture() :String {
        var profilePic = ""
        viewModelScope.launch {
            profilePic = authenticationRepository.getProfilePicture()
        }
        return profilePic
    }

    fun updateProfilePicture(url:String) {
        viewModelScope.launch {
            authenticationRepository.updateProfilePhoto(url)
            _userPicture.value = getUserProfilePicture()
        }
    }

    fun logOut() {
        authenticationRepository.logout()
    }

}