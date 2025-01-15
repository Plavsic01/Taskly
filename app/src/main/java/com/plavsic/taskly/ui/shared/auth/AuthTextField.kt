package com.plavsic.taskly.ui.shared.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.plavsic.taskly.ui.theme.DarkGray
import com.plavsic.taskly.ui.theme.Gray
import com.plavsic.taskly.ui.theme.LightBlack

@Composable
fun AuthTextField(
    state: MutableState<String>,
    placeholder:String,
    isPassword:Boolean,
    onValueChange:(String) -> Unit
) {

    val keyboardOptions = if(!isPassword)
        KeyboardOptions(keyboardType = KeyboardType.Password) else
            KeyboardOptions(keyboardType = KeyboardType.Email)


    TextField(
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = LightBlack,
            unfocusedPlaceholderColor = DarkGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = keyboardOptions,
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Gray, shape = RoundedCornerShape(8.dp))
        ,
        placeholder = {
            Text(text = placeholder)
        },
        singleLine = true,
        visualTransformation = if(isPassword) PasswordVisualTransformation() else
                                VisualTransformation.None,
        value = state.value,
        onValueChange = {
            onValueChange(it)
        }
    )
}