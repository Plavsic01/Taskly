package com.plavsic.taskly.ui.homeScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {


}