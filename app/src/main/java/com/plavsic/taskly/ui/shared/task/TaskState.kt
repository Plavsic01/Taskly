package com.plavsic.taskly.ui.shared.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.plavsic.taskly.core.UIState

@Composable
fun TaskState(
    state: State<UIState<Any>>,
    onIdle:() -> Unit = {},
    onLoading:() -> Unit,
    onSuccess:(Any) -> Unit,
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
            val data = (state.value as UIState.Success<Any>).data
            onSuccess(data)
        }

        is UIState.Error -> {
            onError()
        }
    }
}