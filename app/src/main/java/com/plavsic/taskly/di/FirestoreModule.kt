package com.plavsic.taskly.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.plavsic.taskly.data.task.TaskRepositoryImpl
import com.plavsic.taskly.domain.task.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestore() = Firebase.firestore

    @Provides
    @Singleton
    fun provideTaskRepository(
        firestore: FirebaseFirestore
    ) : TaskRepository {
        return TaskRepositoryImpl(firestore)
    }
}