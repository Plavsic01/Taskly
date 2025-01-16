package com.plavsic.taskly.ui.homeScreen


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.plavsic.taskly.R
import com.plavsic.taskly.ui.shared.common.TasklyTextField
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.Gray


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val offsetY = screenHeightDp * 0.1f

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = offsetY),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    painter = painterResource(R.drawable.sort),
                    contentDescription = "Sort"
                )
                Text("Home")
                Icon(
                    painter = painterResource(R.drawable.sort),
                    contentDescription = "user"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(230.dp),
                painter = painterResource(id = R.drawable.checklist),
                contentDescription = "Checklist"
            )
            Text(
                text = "What do you want to do today?",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Tap + to add your tasks",
                fontSize = 16.sp
            )
        }
    }
}


@Composable
fun AddTaskDialog(
    showDialog:MutableState<Boolean>
){
    val taskTitle = remember { mutableStateOf("") }
    val taskDescription = remember { mutableStateOf("") }

    if(showDialog.value) {
        Dialog(onDismissRequest = {
            showDialog.value = false
        }) {
            Card(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = DarkerGray
                )
            ) {
                Column(
                    modifier = Modifier.padding(all = 20.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 8.dp),
                        text = "Add Task",
                        fontSize = 18.sp
                    )


                    TasklyTextField(
                        state = taskTitle,
                        placeholder = "Task",
                        unfocusedContainerColor = DarkerGray,
                        showBorder = false,
                        onValueChange = {
                            taskTitle.value = it
                        }
                    )


                    TasklyTextField(
                        state = taskDescription,
                        placeholder = "Description",
                        unfocusedContainerColor = DarkerGray,
                        showBorder = false,
                        onValueChange = {
                            taskDescription.value = it
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ICONS

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(R.drawable.timer),
                                contentDescription = "Timer"
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Icon(
                                painter = painterResource(R.drawable.tag),
                                contentDescription = "Tag"
                            )

                            Spacer(modifier = Modifier.width(15.dp))

                            Icon(
                                painter = painterResource(R.drawable.flag),
                                contentDescription = "Flag"
                            )
                        }

                        Icon(
                            painter = painterResource(R.drawable.send),
                            contentDescription = "Send",
                            tint = Color(0xFF8687E7)
                        )
                    }




                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}


