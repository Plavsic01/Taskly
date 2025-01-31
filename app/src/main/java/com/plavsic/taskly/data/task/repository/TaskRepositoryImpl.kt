package com.plavsic.taskly.data.task.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
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
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : TaskRepository {

    private val userId = auth.currentUser!!.uid

    private val tasksCollection = firestore
        .collection("users")
        .document(userId)
        .collection("tasks")


    override fun getTasks(): Flow<Response<List<Task>>> {
        return callbackFlow {
            try {
                val listenerRegistration = tasksCollection
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            trySend(Response.Error(exception.message ?: "Unknown error"))
                            return@addSnapshotListener
                        }
                        val tasks = snapshot?.documents?.map {
                            val taskId = it.getString("taskId") ?: ""
                            val title = it.getString("title") ?: ""
                            val description = it.getString("description") ?: ""
                            val date = it.get("date").toString().toLocalDate()
                            val priority = it.get("priority") as? Map<*, *>
                            val category = it.get("category") as? Map<*,*>
                            val isCompleted = it.getBoolean("isCompleted") ?: false

                            Task(
                                taskId,
                                title,
                                description,
                                date,priority?.toPriority(),
                                category?.toCategory(),
                                isCompleted
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
        val uuid = UUID.randomUUID().toString()

            val taskMap = hashMapOf(
                "taskId" to uuid,
                "title" to task.title,
                "description" to task.description,
                "date" to task.date?.toFirebaseString(),
                "priority" to task.priority,
                "category" to task.category,
                "isCompleted" to task.isCompleted,
            )


            tasksCollection
                .document(uuid)
                .set(taskMap)
                .await()

            Log.i("Firestore","Document created successfully")

    }

    override suspend fun updateTask(task: Task) {
        val taskMap = hashMapOf(
            "taskId" to task.taskId,
            "title" to task.title,
            "description" to task.description,
            "date" to task.date?.toFirebaseString(),
            "priority" to task.priority,
            "category" to task.category,
            "isCompleted" to task.isCompleted,
        )

        tasksCollection
            .document(task.taskId)
            .update(taskMap)
            .await()

        Log.i("Firestore","Document updated successfully")

    }

    override suspend fun deleteTask(taskId:String) {
        tasksCollection
            .document(taskId)
            .delete()
            .await()
    }
}




