package com.plavsic.taskly.ui.shared.task

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.plavsic.taskly.R
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.notification.TaskNotification
import com.plavsic.taskly.ui.shared.calendar.CalendarDialog
import com.plavsic.taskly.ui.shared.calendar.TimeDialog
import com.plavsic.taskly.ui.shared.category.CategoryDialog
import com.plavsic.taskly.ui.shared.common.TasklyTextField
import com.plavsic.taskly.ui.theme.Black
import com.plavsic.taskly.ui.theme.DarkerGray
import java.time.LocalTime

@Composable
fun AddTaskDialog(
    showDialog: Boolean,
    navController:NavHostController,
    dialogViewModel: DialogViewModel,
    taskViewModel: TaskViewModel,
) {
    val showCalendarDialog = remember { mutableStateOf(false) }
    val showTaskPriorityDialog = remember { mutableStateOf(false) }
    val showtimeDialog = remember { mutableStateOf(false) }

    val taskTitle = rememberSaveable { mutableStateOf("") }
    val taskDescription = rememberSaveable { mutableStateOf("") }
    val selectedTime = rememberSaveable { mutableStateOf<LocalTime?>(null) }

    val selectedDate by dialogViewModel.selectedDate
    val selectedCategory by dialogViewModel.selectedCategory
    val selectedPriority by dialogViewModel.selectedPriority

    var isLoading by remember { mutableStateOf(false) }

    val uiState = taskViewModel.uiState.collectAsStateWithLifecycle()

    if (showDialog) {
        Dialog(onDismissRequest = {
            dialogViewModel.hideTaskDialog()
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
                        Row(
                        ) {
                            TaskIcon(
                                image = R.drawable.calendar_outline,
                                contentDescription = "Calendar",
                                onClick = { showCalendarDialog.value = true }
                            )

                            TaskIcon(
                                image = R.drawable.timer,
                                contentDescription = "Time",
                                onClick = { showtimeDialog.value = true }
                            )

                            TaskIcon(
                                image = R.drawable.tag,
                                contentDescription = "Tag",
                                onClick = { dialogViewModel.showCategoryDialog() }
                            )

                            TaskIcon(
                                image = R.drawable.flag,
                                contentDescription = "Flag",
                                onClick = { showTaskPriorityDialog.value = true }
                            )
                        }

                        IconButton(
                            enabled = taskTitle.value.isNotEmpty() &&
                                    taskDescription.value.isNotEmpty()
                                    && !isLoading
                                    && checkIfStatesAreNull(selectedDate,selectedCategory,selectedPriority,selectedTime),
                            onClick = {
                                val task = Task(
                                    title = taskTitle.value,
                                    description = taskDescription.value,
                                    date = selectedDate?.atTime(selectedTime.value!!.hour,selectedTime.value!!.minute),
                                    priority = selectedPriority,
                                    category = selectedCategory,
                                    isCompleted = false,
                                )

                                taskViewModel.addTask(task)

                                // SET TO DEFAULT VALUES AFTER ADDING TASK
                                taskTitle.value = ""
                                taskDescription.value = ""
                                selectedTime.value = null
                                dialogViewModel.clearSelectedDate()
                                dialogViewModel.clearSelectedPriority()
                                dialogViewModel.clearSelectedCategory()

                                dialogViewModel.hideTaskDialog()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.send),
                                contentDescription = "Send",
                                tint = if(taskTitle.value.isNotEmpty()
                                    &&
                                    taskDescription.value.isNotEmpty()
                                    && !isLoading
                                    && checkIfStatesAreNull(selectedDate,selectedCategory,selectedPriority,selectedTime))
                                    Color(0xFF8687E7) else Black
                            )
                        }
                    }
                }
            }
        }
    }
    CalendarDialog(
        showDialog = showCalendarDialog,
        dialogViewModel = dialogViewModel,
    )

    TimeDialog(
        showDialog = showtimeDialog,
        onSubmit = {
            selectedTime.value = it
        }
    )

    CategoryDialog(
        navController = navController,
        dialogViewModel = dialogViewModel
    )

    TaskPriorityDialog(
        showDialog = showTaskPriorityDialog,
        dialogViewModel = dialogViewModel,
    )


    TaskState(
        state = uiState,
        onLoading = {
            isLoading = true
        },
        onSuccess = {
            isLoading = false
        },
        onError = {}
    )
}


@Composable
fun TaskIcon(
    image:Int,
    contentDescription:String,
    onClick:() -> Unit
) {
    IconButton(
        modifier = Modifier
            .width(40.dp),
        onClick = {
           onClick()
        }
    ) {
        Icon(
            painter = painterResource(image),
            contentDescription = contentDescription
        )
    }
}



fun checkIfStatesAreNull(vararg states:Any?) : Boolean {
    return states.all { state ->
        state != null
    }
}

