package com.plavsic.taskly.ui.homeScreen

import androidx.lifecycle.ViewModel
import com.plavsic.taskly.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

}