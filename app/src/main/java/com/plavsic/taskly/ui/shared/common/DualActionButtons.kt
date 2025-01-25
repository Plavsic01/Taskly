package com.plavsic.taskly.ui.shared.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plavsic.taskly.ui.theme.Purple

@Composable
fun DualActionButtons(
    onClickBtn1:() -> Unit,
    onClickBtn2:() -> Unit,
    enabled:Boolean = true,
    btn1Text:String,
    btn2Text:String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TasklyButton(
            onClick = {
                onClickBtn1()
            },
            text = btn1Text,
            containerColor = Color.Transparent,
            contentColor = Purple
        )
        TasklyButton(
            onClick = {
                onClickBtn2()
            },
            enabled = enabled,
            text = btn2Text,
            containerColor = Purple,
            contentColor = Color.White
        )
    }
}