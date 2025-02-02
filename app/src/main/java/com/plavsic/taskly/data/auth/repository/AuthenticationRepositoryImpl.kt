package com.plavsic.taskly.data.auth.repository


import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.core.UIState
import com.plavsic.taskly.domain.auth.model.UserInfo
import com.plavsic.taskly.domain.auth.repository.AuthenticationRepository
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

    override fun userUid(): String = auth.currentUser?.uid ?: ""

    override fun currentUser(): FirebaseUser? = auth.currentUser

    override suspend fun updateUsername(username: String) {
        val userInfo = firestore
            .collection("users")
            .document(userUid())
        userInfo.update("username",username).await()
    }

    override suspend fun updateProfilePicture(uri: String) {
        val userInfo = firestore
            .collection("users")
            .document(userUid())
        userInfo.update("image",uri).await()
    }


    override fun createUserDocument(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        val documentRef = db.collection("users").document(userUid())

        documentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    val user = hashMapOf<String, Any>()
                    documentRef.set(user)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onFailure(e)
                        }
                }else {
                    onSuccess()
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }



    override fun getUserInfo(): Flow<UIState<UserInfo>> {
        val userInfo = firestore
            .collection("users")
            .document(userUid())

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

    override fun logout() {
        auth.signOut()
        Log.i("HOME_SCREEN",FirebaseAuth.getInstance().currentUser?.uid.toString())
        Log.i("HOME_SCREEN",currentUser()?.uid.toString())
        Log.i("HOME_SCREEN",userUid())
    }


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

    override fun reauthenticateAndChangePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess:() -> Unit,
        onFailure:(Exception) -> Unit
    ) {
        val user = currentUser()
        val email = user?.email

        if(user != null && email != null){
            val credential = EmailAuthProvider.getCredential(email,currentPassword)

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    user.updatePassword(newPassword)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener {
                            onFailure(it)
                        }
                }
                .addOnFailureListener {
                        onFailure(it)
                }
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