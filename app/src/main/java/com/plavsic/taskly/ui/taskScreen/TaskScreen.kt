package com.plavsic.taskly.ui.taskScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plavsic.taskly.R
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.Gray
import com.plavsic.taskly.ui.theme.LightBlack
import com.plavsic.taskly.ui.theme.LightWhite
import com.plavsic.taskly.ui.theme.WhiteWithOpacity21

// Screen to show Task and to edit it if needed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .systemBarsPadding(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                ),
                title = {},
                navigationIcon = {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = LightBlack
                        ),
                        onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Close, contentDescription = "Close"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TitleView()

            // Content after Title
            // MAIN ROW

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
//                    .background(color = Gray),
                        ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ROW FOR LEFT SIDE EXAMPLE: ICON AND TASK TIME: etc
                Row {
                    Icon(
                        painter = painterResource(R.drawable.calendar_outline),
                        contentDescription = "Calendar"
                    )
                    Text(
                        text = " Task Time:"
                    )
                }

                TextButton(
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = WhiteWithOpacity21
                    ),
                    onClick = {

                    }
                ) {
                    Text("Sun 26 Jan")
                }
            }
        }
    }

}

@Composable
private fun TitleView() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Do Math Homework",
                fontSize = 20.sp,
            )

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.edit), contentDescription = "Edit"
                )
            }
        }

        Text(
            text = "Do Chapter 2 to 5 for next week",
            fontSize = 16.sp,
            color = LightWhite
        )
    }

}