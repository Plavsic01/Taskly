package com.plavsic.taskly.data.auth.repository

import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.snap
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.core.UIState
import com.plavsic.taskly.domain.auth.model.UserInfo
import com.plavsic.taskly.domain.auth.repository.AuthenticationRepository
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.utils.conversion.toCategory
import com.plavsic.taskly.utils.conversion.toLocalDate
import com.plavsic.taskly.utils.conversion.toPriority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthenticationRepository {

    override suspend fun getProfilePicture() :String {
        return ""
    }

    override suspend fun updateProfilePhoto(photoUrl: String) {
        val user = auth.currentUser

        user?.let {
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUrl))
                .build()

            user.updateProfile(profileUpdate)
                .await()
        }
    }

    override fun userUid(): String = auth.currentUser?.uid ?: ""

    override fun currentUser(): FirebaseUser? = auth.currentUser



    override fun getUserInfo(): Flow<UIState<UserInfo>> {
    val userInfo = firestore
            .collection("users")
            .document(auth.currentUser!!.uid)

        return callbackFlow {
            try {
                val listenerRegistration = userInfo
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            trySend(UIState.Error(exception.message ?: "Unknown error"))
                            return@addSnapshotListener
                        }

                        val username = snapshot?.getString("username") ?: currentUser()!!.email!!.substringBefore("@")
                        val image = snapshot?.getString("image") ?: "https://cdn-icons-png.flaticon.com/512/149/149071.png"

                        trySend(UIState.Success(UserInfo(username,image)))
                    }

                awaitClose {
                    listenerRegistration.remove()
                    channel.close()
                }

            } catch (e: Exception) {
                trySend(UIState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override fun isLoggedIn(): Boolean = auth.currentUser != null

    override fun logout() = auth.signOut()


    override suspend fun login(email: String, password: String): Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)
            val data = auth.signInWithEmailAndPassword(email, password).await()
            emit(Response.Success(data))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Oops, something went wrong."))
        }
    }

    override suspend fun register(email: String, password: String): Flow<Response<AuthResult>> =
        flow {
            try {
                emit(Response.Loading)
                val data = auth.createUserWithEmailAndPassword(email, password).await()
                emit(Response.Success(data))
            } catch (e: Exception) {
                emit(Response.Error(e.localizedMessage ?: "Oops, something went wrong."))
            }
        }

    override suspend fun resetPassword(email: String): Flow<Response<Void?>> = flow {
        try {
            emit(Response.Loading)
            val data = auth.sendPasswordResetEmail(email).await()
            emit(Response.Success(data))
        } catch (e: Exception) {
            emit(Response.Error(e.localizedMessage ?: "Oops, something went wrong."))
        }
    }



    // GOOGLE AUTH

    override suspend fun loginWithGoogle(idToken: String) : Flow<Response<AuthResult>> = flow {
        try {
            emit(Response.Loading)
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken,null)
            val data = auth.signInWithCredential(firebaseCredential).await()
            emit(Response.Success(data))
        }catch (e:Exception){
            emit(Response.Error(e.localizedMessage ?: "Oops, something went wrong."))
        }
    }






}