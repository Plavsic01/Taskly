package com.plavsic.taskly.ui.homeScreen


import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.plavsic.taskly.R
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.auth.model.UserInfo
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.navigation.NavigationGraph
import com.plavsic.taskly.navigation.NoRippleInteractionSource
import com.plavsic.taskly.ui.profileScreen.ProfileViewModel
import com.plavsic.taskly.ui.shared.task.TaskState
import com.plavsic.taskly.ui.shared.task.TaskView
import com.plavsic.taskly.ui.shared.task.TaskViewModel
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.utils.gson.GsonInstance
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    profileViewModel: ProfileViewModel
) {
    val tasksState = taskViewModel.tasksState.collectAsStateWithLifecycle()
    val userInfo = profileViewModel.userInfo.collectAsStateWithLifecycle()
    var userData by remember { mutableStateOf(UserInfo("","")) }

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .padding(horizontal = 15.dp, vertical = 10.dp),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                ),
                title = {
                    Text(
                        text = "Home",
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {},
                actions = {
                    AsyncImage(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(shape = CircleShape),
                        model = userData.image,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop
                    )
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GetTasksState(
                state = tasksState,
                onLoading = {
                    CircularProgressIndicator()
                },
                onSuccess = { list ->
                    if(list.isNotEmpty() && list.any { task ->
                            task.date?.toLocalDate() == LocalDate.now()
                        }){
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize()
                                .padding(top = 70.dp, bottom = 100.dp),
                            verticalArrangement = Arrangement.Top
                        ) {

                            TextButton(
                                interactionSource = NoRippleInteractionSource(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White,
                                    containerColor = DarkerGray
                                ),
                                shape = RoundedCornerShape(4.dp),
                                onClick = {}
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(end = 5.dp),
                                    text = "Today",
                                    fontSize = 12.sp
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_down),
                                    contentDescription = "Arrow Down"
                                )
                            }


                            TasksLazyColumn(
                                tasks = list.filter { task ->
                                    task.date?.toLocalDate() == LocalDate.now() && !task.isCompleted
                                }.sortedBy { it.date },
                                min = 0.dp,
                                max = 1000.dp, //350
                                onClick = {task ->
                                    val encodedTask = Uri.encode(GsonInstance.gson.toJson(task))
                                    navController.navigate("${NavigationGraph.TaskScreen.route}/$encodedTask")
                                }
                            )

                            TextButton(
                                interactionSource = NoRippleInteractionSource(),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Color.White,
                                    containerColor = DarkerGray
                                ),
                                shape = RoundedCornerShape(4.dp),
                                onClick = {}
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(end = 5.dp),
                                    text = "Completed",
                                    fontSize = 12.sp
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_down),
                                    contentDescription = "Arrow Down"
                                )
                            }

                            TasksLazyColumn(
                                tasks = list.filter { task ->
                                    task.isCompleted && task.date?.toLocalDate() == LocalDate.now()
                                }.sortedBy { it.date },
                                min = 0.dp,
                                max = 1000.dp,
                                onClick = {task ->
                                    val encodedTask = Uri.encode(GsonInstance.gson.toJson(task))
                                    navController.navigate("${NavigationGraph.TaskScreen.route}/$encodedTask")
                                }
                            )

                        }

                    }else {
                        NoDataView()
                    }
                },
                onError = {
                    NoDataView()
                }
            )
        }
    }
    TaskState(
        state = userInfo,
        onLoading = {},
        onSuccess = {
            val data = it as UserInfo
            userData = data
        },
        onError = {}
    )
}

@Composable
private fun NoDataView() {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksLazyColumn(
    tasks: List<Task>,
    min:Dp,
    max:Dp,
    isForAlerts:Boolean = false,
    onClick:(Task) -> Unit,
    onLongPress:(Task) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .heightIn(min = min,max = max),
        contentPadding = PaddingValues(vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(tasks) { task ->
            TaskView(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            onClick(task)
                        },
                        onLongClick = {
                            onLongPress(task)
                        }
                    ),
                task = task,
                isForAlerts = isForAlerts,
            )
        }
    }
}


@Composable
fun GetTasksState(
    state:State<Response<List<Task>>>,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (List<Task>) -> Unit,
    onError:@Composable (String) -> Unit
){
    when(state.value){
        is Response.Loading -> {
            onLoading()
        }

        is Response.Success -> {
            val tasks = (state.value as Response.Success<List<Task>>).data
            onSuccess(tasks)
        }

        is Response.Error -> {
            val error = state.value as Response.Error
            onError(error.message)
        }
    }
}



