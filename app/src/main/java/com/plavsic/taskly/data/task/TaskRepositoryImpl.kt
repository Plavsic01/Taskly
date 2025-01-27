package com.plavsic.taskly.data.task

import com.google.firebase.firestore.FirebaseFirestore
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.domain.task.repository.TaskRepository
import com.plavsic.taskly.utils.conversion.toCategory
import com.plavsic.taskly.utils.conversion.toFirebaseString
import com.plavsic.taskly.utils.conversion.toLocalDate
import com.plavsic.taskly.utils.conversion.toPriority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskRepository {

    override fun getTasks(): Flow<Response<List<Task>>> {
        return callbackFlow {
            try {
                val listenerRegistration = firestore.collection("users")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            trySend(Response.Error(exception.message ?: "Unknown error"))
                            return@addSnapshotListener
                        }
                        val tasks = snapshot?.documents?.map {
                            val title = it.getString("title") ?: ""
                            val description = it.getString("description") ?: ""
                            val date = it.get("date").toString().toLocalDate()
                            val priority = it.get("priority") as? Map<*, *>
                            val category = it.get("category") as? Map<*,*>
                            val isCompleted = it.getBoolean("isCompleted") ?: false

                            Task(
                                title,
                                description,
                                date,priority?.toPriority()
                                ,category?.toCategory()
                                ,isCompleted
                            )
                        }
                            trySend(Response.Success(tasks ?: emptyList()))

                    }

                awaitClose {
                    listenerRegistration.remove()
                    channel.close()
                }

            } catch (e: Exception) {
                trySend(Response.Error(e.message ?: "Unknown error"))
            }
        }
    }

    override suspend fun addTask(task:Task) {

        val taskMap = hashMapOf(
            "title" to task.title,
            "description" to task.description,
            "date" to task.date?.toFirebaseString(),
            "priority" to task.priority,
            "category" to task.category,
            "isCompleted" to task.isCompleted
        )

        firestore.collection("users").add(taskMap).await()
    }
}


