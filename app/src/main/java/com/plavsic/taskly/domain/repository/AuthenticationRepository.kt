package com.plavsic.taskly.domain.repository

import com.google.firebase.auth.AuthResult
import com.plavsic.taskly.core.Response
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun login(email: String, password: String): Flow<Response<AuthResult>>

    suspend fun register(email: String, password: String): Flow<Response<AuthResult>>

    suspend fun resetPassword(email: String): Flow<Response<Void?>>

    suspend fun logout()

    suspend fun userUid(): String

    suspend fun isLoggedIn(): Boolean


    // Google Auth

    suspend fun loginWithGoogle(idToken:String) : Flow<Response<AuthResult>>
}