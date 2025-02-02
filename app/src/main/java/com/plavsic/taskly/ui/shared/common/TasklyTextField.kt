package com.plavsic.taskly.ui.shared.common


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.plavsic.taskly.ui.theme.DarkGray
import com.plavsic.taskly.ui.theme.Gray
import com.plavsic.taskly.ui.theme.LightBlack

@Composable
fun TasklyTextField(
    modifier: Modifier = Modifier,
    state: MutableState<String>,
    placeholder:String = "",
    isPassword:Boolean = false,
    isEmail:Boolean = false,
    showBorder:Boolean = true,
    label: @Composable (() -> Unit)? = null,
    unfocusedContainerColor:Color = LightBlack,
    onValueChange:(String) -> Unit
) {

    var showBorderState by remember { mutableStateOf(showBorder) }

    val keyboardOptions = when {
        isEmail -> KeyboardOptions(keyboardType = KeyboardType.Email)
        isPassword ->  KeyboardOptions(keyboardType = KeyboardType.Password)
        else -> KeyboardOptions(keyboardType = KeyboardType.Text)
    }

    TextField(
        label = label,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = unfocusedContainerColor,
            unfocusedPlaceholderColor = DarkGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .addBorder(showBorderState){
                border(1.dp, Gray, shape = RoundedCornerShape(8.dp))
            }
            .onFocusChanged {
                if(!showBorder){
                    showBorderState = it.isFocused
                }
            },
        placeholder = {
            Text(
                text = placeholder
            )
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

private fun Modifier.addBorder(
    condition:Boolean,
    modifier: Modifier.() -> Modifier
): Modifier {
    return if(condition){
        this.then(modifier())
    }else{
        this
    }
}

