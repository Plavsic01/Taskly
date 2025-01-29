package com.plavsic.taskly.ui.taskScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.plavsic.taskly.R
import com.plavsic.taskly.core.UIState
import com.plavsic.taskly.domain.category.model.CategoryIcon
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.ui.shared.calendar.CalendarDialog
import com.plavsic.taskly.ui.shared.category.CategoryDialog
import com.plavsic.taskly.ui.shared.common.Divider
import com.plavsic.taskly.ui.shared.common.DualActionButtons
import com.plavsic.taskly.ui.shared.common.TasklyButton
import com.plavsic.taskly.ui.shared.common.TasklyTextField
import com.plavsic.taskly.ui.shared.task.DialogViewModel
import com.plavsic.taskly.ui.shared.task.TaskPriorityDialog
import com.plavsic.taskly.ui.shared.task.TaskState
import com.plavsic.taskly.ui.shared.task.TaskViewModel
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.LightBlack
import com.plavsic.taskly.ui.theme.LightWhite
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.ui.theme.WhiteWithOpacity21

// Screen to show Task and to edit it if needed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    task: Task,
    navController:NavHostController,
    dialogViewModel: DialogViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    // Need this for changing Task Category Icon maybe implement this
    val editTask = remember { mutableStateOf(task) }

    val showEditDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    val showPriorityDialog = remember { mutableStateOf(false) }
    val showCalendarDialog = remember { mutableStateOf(false) }

    val selectedDate by dialogViewModel.selectedDate
    val selectedCategory by dialogViewModel.selectedCategory
    val selectedPriority by dialogViewModel.selectedPriority

    var isLoading by remember { mutableStateOf(false) }

    val updatedUiState = taskViewModel.updatedUiState.collectAsStateWithLifecycle()

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
                        onClick = {
                            // Close Task Screen
                            navController.popBackStack()
                        }) {
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
                .padding(paddingValues),
        ) {
            TitleView(
                task = editTask.value,
                showEditDialog = showEditDialog
            )

            Spacer(modifier = Modifier.height(20.dp))

            // CONTENT
            Content(
                task = editTask.value,
                onClickCalendar = {
                    showCalendarDialog.value = true
                },
                onClickCategory = {
                    dialogViewModel.showCategoryDialog()
                },
                onClickPriority = {
                    showPriorityDialog.value = true
                },
                onDeleteTask = {
                    showDeleteDialog.value = true
                },
                onEdit = {
                    taskViewModel.updateTask(editTask.value)
                },
                onEnable = {
                    task != editTask.value && !isLoading

                }
            )

            EditTaskDialog(
                showDialog = showEditDialog,
                task = editTask
            )

            CalendarDialog(
                showDialog = showCalendarDialog,
                isForEdit = true,
                onEdit = {
                    editTask.value = editTask.value.copy(
                        date = selectedDate
                    )
                },
                dialogViewModel = dialogViewModel
            )

            CategoryDialog(
                navController = navController,
                isForEdit = true,
                onEdit = {
                    editTask.value = editTask.value.copy(
                        category = selectedCategory
                    )
                },
                dialogViewModel = dialogViewModel
            )

            TaskPriorityDialog(
                showDialog = showPriorityDialog,
                isForEdit = true,
                dialogViewModel = dialogViewModel,
                onEdit = {
                    editTask.value = editTask.value.copy(
                        priority = selectedPriority
                    )
                }
            )

            DeleteTaskDialog(
                showDialog = showDeleteDialog,
                taskTitle = task.title,
                onDelete = {
                    taskViewModel.deleteTask(task.taskId)
                    navController.popBackStack()
                }
            )

        }
    }
    TaskState(
        state = updatedUiState,
        onLoading = {
            isLoading = true
        },
        onSuccess = {
            navController.popBackStack()
        },
        onError = {}
    )
}

@Composable
private fun Content(
    // Maybe add parameter task for onDeleteTask
    task:Task,
    onEnable:() -> Boolean,
    onClickCalendar:() -> Unit,
    onClickCategory:() -> Unit,
    onClickPriority:() -> Unit,
    onDeleteTask:() -> Unit,
    onEdit:() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            ItemRow(
                icon = R.drawable.calendar_outline,
                contentDescription = "Calendar",
                text = " Task Time:",
                btnText = task.date.toString(),
                onClick = {
                    onClickCalendar()
                },
                content = {}
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TASK CATEGORY

            ItemRow(
                icon = R.drawable.tag,
                contentDescription = "Task Category",
                text = " Task Category:",
                btnText = task.category?.name.toString(),
                onClick = {
                    onClickCategory()
                }
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 3.dp),
                    painter = painterResource(CategoryIcon.fromName(task.category!!.image)!!.resId),
                    contentDescription = task.category.name
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // TASK PRIORITY

            ItemRow(
                icon = R.drawable.flag,
                contentDescription = "Task Priority",
                text = " Task Priority:",
                btnText = task.priority?.number.toString(),
                onClick = {
                    onClickPriority()
                },
                content = {
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 3.dp),
                        painter = painterResource(id = R.drawable.flag),
                        contentDescription = "Task Priority ${task.priority!!.number}"
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // DELETE TASK

            Row(
                modifier = Modifier.clickable {
                    onDeleteTask()
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.trash),
                    contentDescription = "Delete"
                )
                Text(
                    text = " Delete Task",
                    color = Color.Red
                )
            }
        }

        Column {
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp),
            ) {
                TasklyButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = onEnable(),
                    onClick = {
                        // Edit Task Button
                        onEdit()
                        Log.i("EditedTask",task.toString())

                    },
                    text = "Edit Task",
                    containerColor = Purple,
                    contentColor = Color.White
                )
            }
        }
    }
}




@Composable
private fun TitleView(
    task: Task,
    showEditDialog:MutableState<Boolean>
) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = task.title,
                fontSize = 20.sp,
            )

            IconButton(
                onClick = {
                    showEditDialog.value = true
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.edit_task),
                    contentDescription = "Edit"
                )
            }
        }

        Text(
            text = task.description,
            fontSize = 16.sp,
            color = LightWhite
        )
    }
}

@Composable
private fun ItemRow(
    icon: Int,
    contentDescription:String,
    text:String,
    btnText:String,
    onClick:() -> Unit,
    content:@Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row {
            Icon(
                painter = painterResource(icon),
                contentDescription = contentDescription
            )
            Text(
                text = text
            )
        }

        TextButton(
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.textButtonColors(
                containerColor = WhiteWithOpacity21
            ),
            onClick = {
                onClick()
            }
        ) {
            content()
            Text(
                text = btnText,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun EditTaskDialog(
    showDialog: MutableState<Boolean>,
    task: MutableState<Task>,
) {
    val taskTitle = rememberSaveable { mutableStateOf(task.value.title) }
    val taskDescription = rememberSaveable { mutableStateOf(task.value.description) }


    TaskDialog(
        showDialog = showDialog,
        title = "Edit Task",
        content = {
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
            DualActionButtons(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                btn1Text = "Cancel",
                btn2Text = "Edit",
                onClickBtn1 = {
                    // Cancel
                    showDialog.value = false
                },
                onClickBtn2 = {
                    task.value = task.value.copy(
                        title = taskTitle.value,
                        description = taskDescription.value
                    )
                    showDialog.value = false
                }
            )
        }
    )
}

@Composable
fun DeleteTaskDialog(
    showDialog: MutableState<Boolean>,
    taskTitle:String,
    onDelete:() -> Unit
) {
    TaskDialog(
      showDialog = showDialog,
      title = "Delete Task",
      content = {

          Spacer(
              modifier = Modifier
                  .height(20.dp)
          )
          Column(
              modifier = Modifier
                  .fillMaxSize(),
              verticalArrangement = Arrangement.SpaceBetween
          ) {
              Text(
                  text = "Are you sure you want to delete this task?",
                  fontSize = 14.sp
              )

              Text(
                  text = "Task title: $taskTitle",
                  fontSize = 14.sp
              )

              DualActionButtons(
                  modifier = Modifier
                      .weight(1f)
                      .padding(horizontal = 10.dp),
                  btn1Text = "Cancel",
                  btn2Text = "Delete",
                  onClickBtn1 = {
                      // Cancel
                      showDialog.value = false
                  },
                  onClickBtn2 = {
                      showDialog.value = false
                      onDelete()
                  }
              )
          }
      }
    )
}


@Composable
fun TaskDialog(
    showDialog:MutableState<Boolean>,
    title:String,
    content:@Composable ColumnScope.() -> Unit
) {
    if (showDialog.value) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
            }
        ) {
            Card(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = DarkerGray
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 20.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        text = title,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    Divider()

                    content()
                }
            }
        }
    }
}


//@Composable
//fun UpdateTaskState(
//    state:State<UIState<Unit>>,
//    onLoading:() -> Unit,
//    onSuccess:() -> Unit,
//    onError:() -> Unit
//) {
//    when(state.value){
//        is UIState.Idle -> {}
//
//        is UIState.Loading -> {
//            onLoading()
//        }
//        is UIState.Success -> {
//            onSuccess()
//        }
//
//        is UIState.Error -> {
//            onError()
//        }
//    }
//}