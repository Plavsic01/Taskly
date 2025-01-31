package com.plavsic.taskly.ui.homeScreen


import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.plavsic.taskly.R
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.navigation.NavigationGraph
import com.plavsic.taskly.ui.profileScreen.ProfileViewModel
import com.plavsic.taskly.ui.shared.common.TasklyTextField
import com.plavsic.taskly.ui.shared.task.TaskView
import com.plavsic.taskly.ui.shared.task.TaskViewModel
import com.plavsic.taskly.utils.gson.GsonInstance


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    profileViewModel: ProfileViewModel
) {
    val tasksState = taskViewModel.tasksState.collectAsStateWithLifecycle()
    var showSearch by remember { mutableStateOf(false) }
    val search = remember { mutableStateOf("") }

    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val offsetY = screenHeightDp * 0.1f

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = offsetY),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.sort),
                        contentDescription = "Sort"
                    )
                    Text("Home")

                    AsyncImage(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(shape = CircleShape),
                        model = profileViewModel.getUserProfilePicture(),
                        contentDescription = "Avatar"
                    )
                }
                if(showSearch){
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                    ){
                        TasklyTextField(
                            state = search,
                            placeholder = "Search for your task...",
                            onValueChange = {input ->
                                search.value = input
                            }
                        )
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            GetTasksState(
                state = tasksState,
                onLoading = {
                    CircularProgressIndicator()
                },
                onSuccess = {
                    if(it.isNotEmpty()){
                        showSearch = true

                        Log.i("TASK_LOADED",it.toString())

                        TasksLazyColumn(
                            tasks = it,
                            onClick = {task ->
                                val encodedTask = Uri.encode(GsonInstance.gson.toJson(task))
                                navController.navigate("${NavigationGraph.TaskScreen.route}/$encodedTask")
                            }
                        )
                    }else {
                        showSearch = false
                        NoDataView()
                    }
                },
                onError = {}
            )
        }
    }

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

@Composable
private fun TasksLazyColumn(
    tasks: List<Task>,
    onClick:(Task) -> Unit
) {
    LazyColumn(
        modifier = Modifier.height(310.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(tasks) { task ->
            TaskView(
                modifier = Modifier.clickable {
                    onClick(task)
                },
                task = task
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
            Log.i("successData","LOADING")
        }

        is Response.Success -> {
            val tasks = (state.value as Response.Success<List<Task>>).data
            Log.i("successData",tasks.toString())
            onSuccess(tasks)
        }

        is Response.Error -> {
            val error = state.value as Response.Error
            onError(error.message)
        }
    }
}



