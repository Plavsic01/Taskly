package com.plavsic.taskly.domain.auth.repository

import com.google.firebase.auth.AuthResult
import com.plavsic.taskly.core.Response
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun login(email: String, password: String): Flow<Response<AuthResult>>

    suspend fun register(email: String, password: String): Flow<Response<AuthResult>>

    suspend fun resetPassword(email: String): Flow<Response<Void?>>

    fun logout()

    fun userUid(): String

    fun isLoggedIn(): Boolean

    suspend fun updateProfilePhoto(photoUrl:String)

    fun getCurrentUserProfilePicture() : String

    // Google Auth
    suspend fun loginWithGoogle(idToken:String) : Flow<Response<AuthResult>>
}