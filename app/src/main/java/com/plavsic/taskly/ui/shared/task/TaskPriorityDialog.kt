package com.plavsic.taskly.ui.shared.task

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.plavsic.taskly.R
import com.plavsic.taskly.ui.shared.common.Divider
import com.plavsic.taskly.ui.shared.common.DualActionButtons
import com.plavsic.taskly.ui.theme.Black
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.Purple
import kotlinx.parcelize.Parcelize

@Composable
fun TaskPriorityDialog(
    showDialog: MutableState<Boolean>,
    isForEdit:Boolean = false,
    onEdit:() -> Unit = {},
    dialogViewModel: DialogViewModel,
){
    val selectedPriority by dialogViewModel.selectedPriority


    val taskPriorities = listOf(
        TaskPriority.FirstPriority,
        TaskPriority.SecondPriority,
        TaskPriority.ThirdPriority,
        TaskPriority.ForthPriority,
        TaskPriority.FifthPriority,
        TaskPriority.SixthPriority,
        TaskPriority.SeventhPriority,
        TaskPriority.EightPriority,
        TaskPriority.NinthPriority,
        TaskPriority.TenthPriority
    )
    if(showDialog.value){
        Dialog(
            onDismissRequest = {
                showDialog.value = false
                dialogViewModel.clearSelectedPriority()
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(370.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkerGray
                )
            ) {

                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp)
                            .fillMaxWidth(),
                        text = "Task Priority",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Divider()
                }

                LazyVerticalGrid(
                    contentPadding = PaddingValues(20.dp),
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(taskPriorities){
                        TaskPriority(
                            taskPriority = it,
                            selectedPriority = selectedPriority,
                            onClick = {
                                dialogViewModel.setSelectedPriority(it)
                            }
                        )
                    }
                }

                DualActionButtons(
                    modifier = Modifier,
                    onClickBtn1 = {
                        showDialog.value = false
                        dialogViewModel.clearSelectedPriority()
                    },
                    onClickBtn2 = {
                        showDialog.value = false
                        onEdit()
                    },
                    enabled = selectedPriority != null,
                    btn1Text = "Cancel",
                    btn2Text = if(!isForEdit) "Save" else "Edit"
                )
            }
        }
    }
}

@Composable
fun TaskPriority(
    taskPriority: TaskPriority,
    selectedPriority: TaskPriority?,
    onClick:() -> Unit
){

    val priorityColor: Color = if(taskPriority == selectedPriority){
        Purple
    }else{
        Black
    }

    Column(
        modifier = Modifier
            .width(64.dp)
            .height(64.dp)
            .background(color = priorityColor, shape = RoundedCornerShape(4.dp))
            .clickable {
                onClick()
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(taskPriority.imageName),
            contentDescription = "Task Priority ${taskPriority.number}"
        )
        Text(
            text = "${taskPriority.number}"
        )
    }
}



@Parcelize
sealed class TaskPriority(val imageName:Int = R.drawable.flag, val number:Int) : Parcelable{
    data object FirstPriority : TaskPriority(number = 1)
    data object SecondPriority : TaskPriority(number = 2)
    data object ThirdPriority : TaskPriority(number = 3)
    data object ForthPriority : TaskPriority(number = 4)
    data object FifthPriority : TaskPriority(number = 5)
    data object SixthPriority : TaskPriority(number = 6)
    data object SeventhPriority : TaskPriority(number = 7)
    data object EightPriority : TaskPriority(number = 8)
    data object NinthPriority : TaskPriority(number = 9)
    data object TenthPriority : TaskPriority(number = 10)
}


