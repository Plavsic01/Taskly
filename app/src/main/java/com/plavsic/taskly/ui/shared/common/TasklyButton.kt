package com.plavsic.taskly.ui.shared.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun TasklyButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text:String,
    containerColor: Color,
    contentColor: Color,
    enabled:Boolean = true
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        onClick = {
            onClick()
        }){
        Text(
            text = text
        )
    }
}






