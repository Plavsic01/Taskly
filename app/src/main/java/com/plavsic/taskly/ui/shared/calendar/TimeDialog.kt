package com.plavsic.taskly.ui.shared.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plavsic.taskly.navigation.NoRippleInteractionSource
import com.plavsic.taskly.ui.shared.common.DualActionButtons
import com.plavsic.taskly.ui.taskScreen.TaskDialog
import com.plavsic.taskly.ui.theme.Black
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.ui.theme.WhiteWithOpacity10
import com.plavsic.taskly.ui.theme.WhiteWithOpacity67
import com.plavsic.taskly.utils.conversion.to24HourFormat
import java.time.LocalTime
import java.util.Locale

@Composable
fun TimeDialog(
    showDialog:MutableState<Boolean>,
    title: String = "Choose Time",
    onSubmit:(LocalTime) -> Unit
) {
    // Hours
    val hours = (1..12).toList()
    val hoursState = rememberLazyListState(initialFirstVisibleItemIndex = Int.MAX_VALUE / 2)

    val selectedHour = remember {
        derivedStateOf {
            val index = hoursState.firstVisibleItemIndex + 1
            hours[index % hours.size]
        }
    }

    // Minutes
    val minutes = (0..59).toList()
    val minutesState = rememberLazyListState(initialFirstVisibleItemIndex = Int.MAX_VALUE / 2)

    val selectedMinute = remember {
        derivedStateOf {
            val index = minutesState.firstVisibleItemIndex + 1
            minutes[index % minutes.size]
        }
    }

    val selectedTimeType = remember { mutableStateOf("AM") }

    TaskDialog(
        showDialog = showDialog,
        title = title,
        height = 270.dp,
        content = {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimeView(
                        state = hoursState,
                        timeList = hours,
                        selectedTime = selectedHour
                    )

                    Text(
                        text = ":",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )

                    TimeView(
                        state = minutesState,
                        timeList = minutes,
                        selectedTime = selectedMinute
                    )

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(color = Black, shape = RoundedCornerShape(4.dp))
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TimeTypeText(
                                title = "AM",
                                selectedTimeType = selectedTimeType
                            )

                            TimeTypeText(
                                title = "PM",
                                selectedTimeType = selectedTimeType
                            )
                        }
                    }
                }

                DualActionButtons(
                    modifier = Modifier.weight(1f),
                    btn1Text = "Cancel",
                    btn2Text = if(title == "Choose Time") "Save" else "Set Alert",
                    onClickBtn1 = {
                        showDialog.value = false
                    },
                    onClickBtn2 = {
                        val time = LocalTime.of(selectedHour.value.to24HourFormat(selectedTimeType.value == "PM")
                            ,selectedMinute.value)
                        showDialog.value = false
                        onSubmit(time)
                    }
                )

            }
        }
    )
}

@Composable
fun TimeTypeText(
    title:String,
    selectedTimeType:MutableState<String>
) {
    TextButton(
        onClick = {
            selectedTimeType.value = title
        },
        interactionSource = NoRippleInteractionSource()
    ) {
        Text(
            text = title,
            fontSize = if(selectedTimeType.value == title) 22.sp else 16.sp,
            color = if(selectedTimeType.value == title) Purple else WhiteWithOpacity67
        )
    }
}

@Composable
fun TimeView(
    state:LazyListState,
    timeList:List<Int>,
    selectedTime:State<Int>
) {
    LazyColumn(
        modifier = Modifier
            .size(90.dp)
            .background(Black, shape = RoundedCornerShape(4.dp)),
        state = state,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
        contentPadding = PaddingValues(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        items(Int.MAX_VALUE) {index ->

            val time = timeList[index % timeList.size]
            val formattedTime = String.format(locale = Locale.ENGLISH, format = "%02d",time)

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = formattedTime,
                fontSize = if(selectedTime.value == time) 24.sp else 18.sp,
                color = if(selectedTime.value == time) Color.White else WhiteWithOpacity10,
                textAlign = TextAlign.Center
            )
        }
    }
}


