package com.plavsic.taskly.ui.shared.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plavsic.taskly.ui.theme.Gray

@Composable
fun SeparatorLine() {
    Row(
        modifier = Modifier.padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Gray)
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 2.dp),
            text = "or",
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Gray)
        )
    }
}