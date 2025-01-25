package com.plavsic.taskly.ui.homeScreen


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plavsic.taskly.R
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.ui.shared.task.TaskViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val tasksState = taskViewModel.tasksState.collectAsStateWithLifecycle()
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }

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

            Text(
                text = tasks.toString(),
                fontSize = 20.sp
            )


            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Tap + to add your tasks",
                fontSize = 16.sp
            )
        }
    }
    GetTasksState(
            state = tasksState,
    onLoading = {},
    onSuccess = {
        tasks = it
    },
    onError = {}
    )
}




@Composable
fun GetTasksState(
    state:State<Response<List<Task>>>,
    onLoading:() -> Unit,
    onSuccess:(List<Task>) -> Unit,
    onError:() -> Unit
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
            onError()
        }
    }
}






@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}


