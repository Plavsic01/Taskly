package com.plavsic.taskly.ui.shared.calendar

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.plavsic.taskly.ui.shared.common.DualActionButtons
import com.plavsic.taskly.ui.theme.DarkerGray
import java.time.LocalDate

@Composable
fun CalendarDialog(
    showDialog: MutableState<Boolean>,
    onSubmit:(LocalDate?) -> Unit
) {
    var selectedDate:LocalDate? by remember { mutableStateOf(null) }

    if(showDialog.value){
        Log.i("selectedDate",selectedDate.toString())
        Dialog(
            onDismissRequest = {
                showDialog.value = false
                selectedDate = null
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
                    selectedDate = it
                }

                DualActionButtons(
                    onClickBtn1 = {
                        showDialog.value = false
                        selectedDate = null
                    },
                    onClickBtn2 = {
                        showDialog.value = false
                        onSubmit(selectedDate)
                    },
                    enabled = selectedDate != null,
                    btn1Text = "Cancel",
                    btn2Text = "Choose Date"
                )
            }
        }
    }

}