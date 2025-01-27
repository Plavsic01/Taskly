package com.plavsic.taskly.ui.taskScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plavsic.taskly.R
import com.plavsic.taskly.domain.category.model.CategoryIcon
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.ui.shared.common.TasklyButton
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.LightBlack
import com.plavsic.taskly.ui.theme.LightWhite
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.ui.theme.WhiteWithOpacity21

// Screen to show Task and to edit it if needed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    task: Task
) {

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
                        onClick = {}) {
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
                task = task
            )
            // Content after Title
            // MAIN ROW
            Spacer(modifier = Modifier.height(20.dp))

            // CONTENT
            Content(
                task = task,
                onDeleteTask = {
                    Log.i("TaskPrebacen",task.toString())
                }
            )
        }
    }
}

@Composable
private fun Content(
    // Maybe add parameter task for onDeleteTask
    task:Task,
    onDeleteTask:() -> Unit
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
                content = {}
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TASK CATEGORY

            ItemRow(
                icon = R.drawable.tag,
                contentDescription = "Task Category",
                text = " Task Category:",
                btnText = task.category?.name.toString()
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
                    onClick = {},
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
    task: Task
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
                onClick = {}
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
    content:@Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ROW FOR LEFT SIDE EXAMPLE: ICON AND TASK TIME: etc
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

            }
        ) {
            content()
            Text(
//                Sun 26 Jan
                text = btnText,
                fontSize = 12.sp
            )
        }
    }
}