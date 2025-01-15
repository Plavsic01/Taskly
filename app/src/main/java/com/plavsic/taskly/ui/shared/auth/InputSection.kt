package com.plavsic.taskly.ui.shared.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputSection(
    state: MutableState<String>,
    text:String,
    isPassword:Boolean = false,
    placeholder:String
) {
    Column {
        Text(
            modifier = Modifier
                .padding(bottom = 8.dp),
            text = text
        )
        AuthTextField(
            state = state,
            placeholder = placeholder,
            isPassword = isPassword,
            onValueChange = {
                state.value = it
            })
    }
}