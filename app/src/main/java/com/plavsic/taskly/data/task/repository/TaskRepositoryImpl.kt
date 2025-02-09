package com.plavsic.taskly.data.task.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.domain.task.repository.TaskRepository
import com.plavsic.taskly.utils.conversion.toCategory
import com.plavsic.taskly.utils.conversion.toPriority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : TaskRepository {


    override fun getTasks(): Flow<Response<List<Task>>> {
        return callbackFlow {
            try {
                val listenerRegistration = getCollection()
                    ?.addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            trySend(Response.Error(exception.message ?: "Unknown error"))
                            return@addSnapshotListener
                        }
                        val tasks = snapshot?.documents?.map {
                            val taskId = it.getString("taskId") ?: ""
                            val title = it.getString("title") ?: ""
                            val description = it.getString("description") ?: ""
                            val dateTimestamp = it.getDate("dateTime")
                            val notificationDateTime = it.getDate("notificationDateTime")
                            val priority = it.get("priority") as? Map<*, *>
                            val category = it.get("category") as? Map<*,*>
                            val isCompleted = it.getBoolean("isCompleted") ?: false


                            val dateTime = dateTimestamp?.toInstant()
                                ?.atZone(ZoneId.systemDefault())
                                ?.toLocalDateTime()

                            val alert = notificationDateTime?.toInstant()
                                ?.atZone(ZoneId.systemDefault())
                                ?.toLocalDateTime()

                            Task(
                                taskId,
                                title,
                                description,
                                dateTime,
                                alert,
                                priority?.toPriority(),
                                category?.toCategory(),
                                isCompleted
                            )
                        }
                            trySend(Response.Success(tasks ?: emptyList()))
                    }

                awaitClose {
                    listenerRegistration?.remove()
                    close()
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
                "dateTime" to Date.from(task.date?.atZone(ZoneId.systemDefault())?.toInstant()),
                "priority" to task.priority,
                "category" to task.category,
                "isCompleted" to task.isCompleted,
            )

            getCollection()
                ?.document(uuid)
                ?.set(taskMap)
                ?.await()

    }


    override suspend fun updateTask(task: Task) {
        val taskMap = hashMapOf(
            "taskId" to task.taskId,
            "title" to task.title,
            "description" to task.description,
            "dateTime" to Date.from(task.date?.atZone(ZoneId.systemDefault())?.toInstant()),
            "priority" to task.priority,
            "category" to task.category,
            "isCompleted" to task.isCompleted,
        )

        getCollection()
            ?.document(task.taskId)
            ?.update(taskMap)
            ?.await()

        Log.i("Firestore","Document updated successfully")
    }

    override suspend fun updateAlertSchedule(taskId:String,alert: LocalDateTime) {
        getCollection()
            ?.document(taskId)
            ?.update("notificationDateTime",Date.from(alert.atZone(ZoneId.systemDefault())?.toInstant()))
            ?.await()
    }

    override suspend fun deleteAlertSchedule(taskId: String) {
        getCollection()
            ?.document(taskId)
            ?.update("notificationDateTime",FieldValue.delete())
            ?.await()
    }

    override suspend fun deleteTask(taskId: String) {
        getCollection()
            ?.document(taskId)
            ?.delete()
            ?.await()
    }


    private fun getCollection(): CollectionReference? {
        val user = auth.currentUser
        return if (user != null) {
            firestore
                .collection("users")
                .document(user.uid)
                .collection("tasks")
        } else {
            null
        }
    }
}






