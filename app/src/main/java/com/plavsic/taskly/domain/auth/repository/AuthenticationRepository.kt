package com.plavsic.taskly.domain.auth.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.core.UIState
import com.plavsic.taskly.domain.auth.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

//    suspend fun getUsername() : String

    suspend fun login(email: String, password: String): Flow<Response<AuthResult>>

    suspend fun register(email: String, password: String): Flow<Response<AuthResult>>

    suspend fun resetPassword(email: String): Flow<Response<Void?>>

    suspend fun updateProfilePhoto(photoUrl:String)

    suspend fun getProfilePicture() : String

    fun userUid(): String

    fun currentUser():FirebaseUser?

    fun getUserInfo() : Flow<UIState<UserInfo>>

    fun isLoggedIn(): Boolean

    fun logout()

    // Google Auth
    suspend fun loginWithGoogle(idToken:String) : Flow<Response<AuthResult>>
}