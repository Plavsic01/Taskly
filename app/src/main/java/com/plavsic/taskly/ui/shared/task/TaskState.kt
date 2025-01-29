package com.plavsic.taskly.ui.shared.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.plavsic.taskly.core.UIState

@Composable
fun TaskState(
    state: State<UIState<Unit>>,
    onIdle:() -> Unit = {},
    onLoading:() -> Unit,
    onSuccess:() -> Unit,
    onError:() -> Unit
) {
    when(state.value){
        is UIState.Idle -> {
            onIdle()
        }

        is UIState.Loading -> {
            onLoading()
        }
        is UIState.Success -> {
            onSuccess()
        }

        is UIState.Error -> {
            onError()
        }
    }
}