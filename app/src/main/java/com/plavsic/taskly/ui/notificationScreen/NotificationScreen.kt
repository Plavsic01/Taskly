package com.plavsic.taskly.ui.notificationScreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plavsic.taskly.R
import com.plavsic.taskly.data.task.repository.AlarmSchedulerImpl
import com.plavsic.taskly.domain.task.model.AlarmTask
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.notification.TaskNotification
import com.plavsic.taskly.ui.homeScreen.GetTasksState
import com.plavsic.taskly.ui.homeScreen.TasksLazyColumn
import com.plavsic.taskly.ui.shared.calendar.CalendarDialog
import com.plavsic.taskly.ui.shared.calendar.HorizontalCalendarView
import com.plavsic.taskly.ui.shared.calendar.TimeDialog
import com.plavsic.taskly.ui.shared.common.DualActionButtons
import com.plavsic.taskly.ui.shared.task.DialogViewModel
import com.plavsic.taskly.ui.shared.task.TaskIcon
import com.plavsic.taskly.ui.shared.task.TaskViewModel
import com.plavsic.taskly.ui.taskScreen.TaskDialog
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.utils.conversion.formatDate
import com.plavsic.taskly.utils.conversion.formatTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotificationScreen(
    taskViewModel: TaskViewModel,
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val alarmScheduler = AlarmSchedulerImpl(context = context)
    val alarmTask = remember { mutableStateOf<AlarmTask?>(null) }

    val tasksState = taskViewModel.tasksState.collectAsStateWithLifecycle()
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }

    val showAlertDialog = remember { mutableStateOf(false) }
    val showDeleteAlert = remember { mutableStateOf(false) }
    val showTimeDialog = remember { mutableStateOf(false) }
    val showCalendarDialog = remember { mutableStateOf(false) }
    var shouldShowSettingsDialog by remember { mutableStateOf(false) }

    val selectedDateState by dialogViewModel.selectedDate
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }

    val currentMonth = remember { mutableStateOf(YearMonth.now()) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if(!isGranted) {
                shouldShowSettingsDialog = true
            }
        }
    )

    LaunchedEffect(Unit) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !TaskNotification.hasNotificationPermission(context = context)) {

            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                ),
                title = {
                    Text(
                        text = "Alerts",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                },
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 100.dp)
        ) {

            HorizontalCalendarView(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                tasks = tasks.filter { task ->
                    !task.isCompleted
                }
            )

            // TODO: ADD TO FILTER THAT ONLY SHOWS TASKS THAT ARE TODAY OR AFTER TODAY

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                TasksLazyColumn(
                    tasks = tasks.filter { it.date?.toLocalDate() == selectedDate.value && !it.isCompleted },
                    isForAlerts = true,
                    min = 100.dp,
                    max = 510.dp,
                    onClick = { task ->
                        // Open Alert Dialog
                        alarmTask.value = AlarmTask(taskId = task.taskId, title = task.title, time = task.date)
                        showAlertDialog.value = true
                    },
                    onLongPress = {task ->
                        // Show Delete Schedule dialog
                        if(task.alert != null) {
                            alarmTask.value = AlarmTask(taskId = task.taskId, title = task.title, time = null)
                            showDeleteAlert.value = true
                        }
                    }
                )
            }
        }
    }

    TaskDialog(
     showDialog = showAlertDialog,
     title = "Create Task Notification",
     content = {
         Column(
             modifier = Modifier.fillMaxSize(),
             verticalArrangement = Arrangement.SpaceAround
         ) {
             Row(
                 modifier = Modifier
                     .fillMaxWidth(),
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.Center
             ) {
                 TaskIcon(
                     image = R.drawable.calendar_outline,
                     contentDescription = "Calendar",
                     onClick = { showCalendarDialog.value = true }
                 )

                 Text(
                     text = "Date: ${selectedDateState?.formatDate()}",
                 )
             }

             Row(
                 modifier = Modifier
                     .fillMaxWidth(),
                 verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.Center
             ) {
                 TaskIcon(
                     image = R.drawable.timer,
                     contentDescription = "Time",
                     onClick = { showTimeDialog.value = true }
                 )

                 Text(
                     text = "Time: ${selectedTime.formatTime()}"
                 )
             }

             Spacer(modifier = Modifier.height(20.dp))

             DualActionButtons(
                 modifier = Modifier
                     .weight(1f),
                 btn1Text = "Cancel",
                 btn2Text = "Create Alert",
                 onClickBtn1 = {
                     showAlertDialog.value = false
                 },
                 onClickBtn2 = {
                     createNotification(
                         taskViewModel,
                         selectedTime,
                         selectedDateState,
                         alarmTask,
                         alarmScheduler,
                         context)
                     showAlertDialog.value = false
                 }
             )
         }
     })

    CalendarDialog(
        showDialog = showCalendarDialog,
        dialogViewModel = dialogViewModel,
    )


    TimeDialog(
        showDialog = showTimeDialog,
        title = "Create Task Notification",
        onSubmit = { time ->
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                !TaskNotification.hasNotificationPermission(context)) {
                shouldShowSettingsDialog = true
            }else {
                selectedTime = time
            }
        }
    )

    TaskDialog(
        showDialog = showDeleteAlert,
        title = "Delete scheduled Alert?",
        height = 200.dp,
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Are you sure you want to delete this alert?",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                DualActionButtons(
                    modifier = Modifier
                        .weight(1f),
                    btn1Text = "Cancel",
                    btn2Text = "Delete Alert",
                    onClickBtn1 = {
                        showDeleteAlert.value = false
                    },
                    onClickBtn2 = {
                        showDeleteAlert.value = false
                        taskViewModel.deleteAlertSchedule(alarmTask.value!!.taskId)
                        alarmScheduler.cancel(task = alarmTask.value!!)
                    }
                )
            }
        }
    )


    GetTasksState(
        state = tasksState,
        onLoading = {
//            CircularProgressIndicator()
        },
        onSuccess = { taskList ->
            tasks = taskList
        },
        onError = {}
    )


    if (shouldShowSettingsDialog) {
        AlertDialog(
            onDismissRequest = { shouldShowSettingsDialog = false },
            title = { Text(modifier = Modifier.fillMaxWidth(),text = "Notification permission is required", fontSize = 20.sp, textAlign = TextAlign.Center) },
            text = { Text(modifier = Modifier.fillMaxWidth(),text = "To use notifications, please enable them in Settings.",textAlign = TextAlign.Center) },
            confirmButton = {
                Button(onClick = {
                    shouldShowSettingsDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:" + context.packageName)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Go to Settings")
                }
            },
            dismissButton = {
                Button(onClick = { shouldShowSettingsDialog = false }) {
                    Text("   Cancel   ")
                }
            }
        )
    }
}

private fun createNotification(
    taskViewModel: TaskViewModel,
    selectedTime: LocalTime,
    selectedDateState: LocalDate?,
    alarmTask: MutableState<AlarmTask?>,
    alarmScheduler: AlarmSchedulerImpl,
    context: Context
) {
    val time = LocalTime.of(selectedTime.hour, selectedTime.minute)
    val dateTme = LocalDateTime.of(selectedDateState, time)
    alarmTask.value = AlarmTask(taskId = alarmTask.value!!.taskId, title = alarmTask.value!!.title , time = dateTme)
    alarmTask.value?.let(alarmScheduler::schedule)
    taskViewModel.updateAlertSchedule(alarmTask.value!!.taskId,dateTme)
    Toast.makeText(context, "Notification created!", Toast.LENGTH_SHORT).show()
}