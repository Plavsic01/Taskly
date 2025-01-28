package com.plavsic.taskly.ui.profileScreen

import androidx.lifecycle.ViewModel
import com.plavsic.taskly.domain.auth.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun getCurrentUserId() : String {
        return authenticationRepository.userUid()
    }

    fun getUserProfilePicture() :String {
        return authenticationRepository.getCurrentUserProfilePicture()
    }

    fun logOut() {
        authenticationRepository.logout()
    }

}