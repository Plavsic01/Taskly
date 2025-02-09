package com.plavsic.taskly.ui.shared.calendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.plavsic.taskly.ui.shared.common.DualActionButtons
import com.plavsic.taskly.ui.shared.task.DialogViewModel
import com.plavsic.taskly.ui.theme.DarkerGray
import java.time.LocalDate

@Composable
fun CalendarDialog(
    showDialog: MutableState<Boolean>,
    editDate:LocalDate? = null,
    isForEdit:Boolean = false,
    onEdit:() -> Unit = {},
    dialogViewModel: DialogViewModel
) {

    val selectedDate by dialogViewModel.selectedDate

    if(showDialog.value){
        if(isForEdit){
            dialogViewModel.setSelectedDate(editDate)
        }else {
            dialogViewModel.setSelectedDate(LocalDate.now())
        }
        Dialog(
            onDismissRequest = {
                showDialog.value = false
                if(!isForEdit && selectedDate != LocalDate.now()){
                    dialogViewModel.setSelectedDate(LocalDate.now())
                }
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(370.dp)
                ,
                colors = CardDefaults.cardColors(
                    containerColor = DarkerGray
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                CalendarView(
                    modifier = Modifier
                        .weight(1f),
                    chosenDate = selectedDate
                ){
                    dialogViewModel.setSelectedDate(it)
                }

                DualActionButtons(
                    modifier = Modifier,
                    onClickBtn1 = {
                        showDialog.value = false
                        if(!isForEdit && selectedDate != LocalDate.now()){
                            dialogViewModel.setSelectedDate(LocalDate.now())
                        }
                    },
                    onClickBtn2 = {
                        showDialog.value = false
                        onEdit()
                    },
                    enabled = selectedDate != null,
                    btn1Text = "Cancel",
                    btn2Text = if(!isForEdit) "Choose Date" else "Edit Date"
                )
            }
        }
    }
}