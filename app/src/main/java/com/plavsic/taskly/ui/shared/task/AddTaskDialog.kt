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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.core.UIState
import com.plavsic.taskly.domain.category.model.Category
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.ui.shared.calendar.CalendarDialog
import com.plavsic.taskly.ui.shared.category.CategoryDialog
import com.plavsic.taskly.ui.shared.common.TasklyTextField
import com.plavsic.taskly.ui.theme.Black
import com.plavsic.taskly.ui.theme.DarkerGray
import java.time.LocalDate

@Composable
fun AddTaskDialog(
    showDialog: Boolean,
    navController:NavHostController,
    dialogViewModel: DialogViewModel,
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val taskTitle = rememberSaveable { mutableStateOf("") }
    val taskDescription = rememberSaveable { mutableStateOf("") }

    val showCalendarDialog = remember { mutableStateOf(false) }
    val showTaskPriorityDialog = remember { mutableStateOf(false) }

    var calendar: LocalDate? by remember { mutableStateOf(null) }
    var category: Category? by remember { mutableStateOf(null) }
    var priority: TaskPriority? by remember { mutableStateOf(null) }

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
                                    painter = painterResource(R.drawable.timer),
                                    contentDescription = "Timer",
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
//                                TODO("ADD FUNCTIONALITY TO CREATE NEW TASK")
                                val task = Task(
                                    title = taskTitle.value,
                                    description = taskDescription.value,
                                    date = calendar,
                                    priority = priority,
                                    category = category,
                                    isCompleted = false
                                )
                                taskViewModel.addTask(task)
                                Log.i("DodanTask",task.toString())
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
        onSubmit = {
            Log.i("DATUM", it.toString())
            calendar = it
        }
    )

    CategoryDialog(
        navController = navController,
        dialogViewModel = dialogViewModel,
        onSelectedCategory = {
            category = it
        }
    )

    TaskPriorityDialog(
        showDialog = showTaskPriorityDialog,
        onSelectedPriority = {
            priority = it
        }
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



