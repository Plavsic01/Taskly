package com.plavsic.taskly.ui.shared.task

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.plavsic.taskly.R
import com.plavsic.taskly.core.UIState
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.ui.shared.calendar.CalendarDialog
import com.plavsic.taskly.ui.shared.category.CategoryDialog
import com.plavsic.taskly.ui.shared.common.TasklyTextField
import com.plavsic.taskly.ui.theme.Black
import com.plavsic.taskly.ui.theme.DarkerGray

@Composable
fun AddTaskDialog(
    showDialog: Boolean,
    navController:NavHostController,
    dialogViewModel: DialogViewModel,
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val showCalendarDialog = remember { mutableStateOf(false) }
    val showTaskPriorityDialog = remember { mutableStateOf(false) }

    val taskTitle = remember { mutableStateOf("") }
    val taskDescription = remember { mutableStateOf("") }

    val selectedDate by dialogViewModel.selectedDate
    val selectedCategory by dialogViewModel.selectedCategory
    val selectedPriority by dialogViewModel.selectedPriority

    var isAddBtnEnabled by remember { mutableStateOf(true) }

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
                        Row {

                            IconButton(
                                onClick = {
                                    showCalendarDialog.value = true
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.calendar_outline),
                                    contentDescription = "Calendar",
                                )
                            }

                            IconButton(
                                onClick = {
                                    dialogViewModel.showCategoryDialog()
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.tag),
                                    contentDescription = "Tag",
                                )
                            }

                            IconButton(
                                onClick = {
                                    showTaskPriorityDialog.value = true
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.flag),
                                    contentDescription = "Flag",
                                )
                            }
                        }


                        IconButton(
                            enabled = isAddBtnEnabled,
                            onClick = {
                                val task = Task(
                                    title = taskTitle.value,
                                    description = taskDescription.value,
                                    date = selectedDate,
                                    priority = selectedPriority,
                                    category = selectedCategory,
                                    isCompleted = false
                                )
                                taskViewModel.addTask(task)

                                // SET TO DEFAULT VALUES AFTER ADDING TASK
                                taskTitle.value = ""
                                taskDescription.value = ""
                                dialogViewModel.clearSelectedDate()
                                dialogViewModel.clearSelectedPriority()
                                dialogViewModel.clearSelectedCategory()
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.send),
                                contentDescription = "Send",
                                tint = if(isAddBtnEnabled) Color(0xFF8687E7) else Black
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

    CategoryDialog(
        navController = navController,
        dialogViewModel = dialogViewModel
    )

    TaskPriorityDialog(
        showDialog = showTaskPriorityDialog,
        dialogViewModel = dialogViewModel,
    )

    AddTaskState(
        state = uiState,
        onLoading = {
            isAddBtnEnabled = false
        },
        onSuccess = {
            isAddBtnEnabled = true
        },
        onError = {}
    )
}


@Composable
fun AddTaskState(
    state:State<UIState<Unit>>,
    onLoading:() -> Unit,
    onSuccess:() -> Unit,
    onError:() -> Unit
) {
    when(state.value){
        is UIState.Idle -> {}

        is UIState.Loading -> {
            onLoading()
            Log.i("successData","LOADING")
        }
        is UIState.Success -> {
            onSuccess()
            Log.i("successData","USPESNO KREIRAN")
        }

        is UIState.Error -> {
            onError()
        }

    }
}



