package com.plavsic.taskly.ui.shared.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.core.UIState
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.domain.task.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState:MutableStateFlow<UIState<Unit>> = MutableStateFlow(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _updatedUiState:MutableStateFlow<UIState<Unit>> = MutableStateFlow(UIState.Idle)
    val updatedUiState = _updatedUiState.asStateFlow()

    val tasksState = taskRepository.getTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Response.Loading
        )

    fun addTask(task: Task) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            try {
                taskRepository.addTask(task)
                _uiState.value = UIState.Success(Unit)
            }catch (e:Exception){
                _uiState.value = UIState.Error(message = e.message!!)
            }
        }
    }

    fun updateTask(task:Task) {
        viewModelScope.launch {
            _updatedUiState.value = UIState.Loading
            try {
                taskRepository.updateTask(task)
                _updatedUiState.value = UIState.Success(Unit)
            }catch (e:Exception) {
                _updatedUiState.value = UIState.Error(message = e.message!!)
            }
        }
    }

    fun deleteTask(taskId:String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }

}


// Ekstenzija za Query -> Flow<QuerySnapshot>
//fun Query.toFlow(): Flow<QuerySnapshot> = callbackFlow {
//    val listener = addSnapshotListener { snapshot, exception ->
//        if (exception != null) {
//            // Propagacija greške kroz Flow
//            close(exception)
//            return@addSnapshotListener
//        }
//        if (snapshot != null) {
//            // Emitovanje podataka kroz Flow
//            trySend(snapshot).isSuccess
//        }
//    }
//    // Zatvaranje listener-a kada Flow završi
//    awaitClose { listener.remove() }
//}

