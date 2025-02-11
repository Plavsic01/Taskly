package com.plavsic.taskly.ui.calendarScreen


import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.navigation.NavigationGraph
import com.plavsic.taskly.navigation.NoRippleInteractionSource
import com.plavsic.taskly.ui.homeScreen.GetTasksState
import com.plavsic.taskly.ui.homeScreen.TasksLazyColumn
import com.plavsic.taskly.ui.shared.calendar.HorizontalCalendarView
import com.plavsic.taskly.ui.shared.task.TaskViewModel
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.Gray
import com.plavsic.taskly.ui.theme.LighterGray
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.utils.gson.GsonInstance
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController:NavHostController,
    taskViewModel: TaskViewModel
) {
    val tasks = taskViewModel.tasksState.collectAsStateWithLifecycle()

    var tasksState by remember { mutableStateOf<List<Task>>(emptyList()) }

    val currentMonth = remember { mutableStateOf(YearMonth.now()) }

    val selectedDate = remember { mutableStateOf(LocalDate.now()) }

    var isTodayClicked by remember { mutableStateOf(true) }

    var isCompletedClicked by remember { mutableStateOf(false) }

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
                        text = "Calendar",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                },
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {

            HorizontalCalendarView(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                tasks = tasksState
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 100.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(color = LighterGray, shape = RoundedCornerShape(4.dp)),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CustomTextButton(
                        text = "Today",
                        isClicked = isTodayClicked,
                        onClick = {
                            isTodayClicked = true
                            isCompletedClicked = false
                        }
                    )

                    CustomTextButton(
                        text = "Completed",
                        isClicked = isCompletedClicked,
                        onClick = {
                            isCompletedClicked = true
                            isTodayClicked = false
                        }
                    )
                }


                TasksLazyColumn(
                    tasks = if(isTodayClicked) tasksState.filter { it.date?.toLocalDate() == selectedDate.value && !it.isCompleted }
                    else
                        tasksState.filter { it.date?.toLocalDate() == selectedDate.value && it.isCompleted },
                    min = 100.dp,
                    max = 400.dp,
                    onClick = { task ->
                        val encodedTask = Uri.encode(GsonInstance.gson.toJson(task))
                        navController.navigate("${NavigationGraph.TaskScreen.route}/$encodedTask")
                    }
                )
            }
        }
    }

    GetTasksState(
        state = tasks,
        onLoading = {
//            CircularProgressIndicator()
        },
        onSuccess = { taskList ->
            tasksState = taskList.sortedBy { it.date }
        },
        onError = {}
    )

}


@Composable
private fun CustomTextButton(
    isClicked:Boolean,
    text:String,
    onClick:() -> Unit
) {
    TextButton(
        modifier = Modifier
            .height(50.dp)
            .width(140.dp),
        interactionSource = NoRippleInteractionSource(),
        border = if(isClicked) null else BorderStroke(width = 2.dp, color = Gray),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.White,
            containerColor = if(isClicked) Purple else LighterGray
        ),
        onClick = {
            onClick()
        }
    ) {
        Text(
            text = text
        )
    }
}


fun displayDayOfWeek(day:String) : Color {
   return when(day) {
        "SATURDAY" -> Color.Red
        "SUNDAY" -> Color.Red
        else -> Color.White
    }
}
