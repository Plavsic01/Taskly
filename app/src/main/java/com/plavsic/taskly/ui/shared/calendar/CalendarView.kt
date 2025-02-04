package com.plavsic.taskly.ui.shared.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.plavsic.taskly.ui.shared.common.Divider
import com.plavsic.taskly.ui.theme.DarkGray
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.LightBlack
import com.plavsic.taskly.ui.theme.Purple
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarView(
    chosenDate: LocalDate?,
    modifier:Modifier = Modifier,
    onSelectedDate:(LocalDate?) -> Unit
){

    var currentMonth by if(chosenDate == null){
        remember { mutableStateOf(YearMonth.now()) }
    }else{
        remember { mutableStateOf(YearMonth.of(chosenDate.year,chosenDate.month)) }
    }

    var selectedDate:LocalDate? by remember { mutableStateOf(chosenDate) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = DarkerGray)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CalendarHeader(
            month = currentMonth,
            onPreviousMonth = {
                currentMonth = currentMonth.minusMonths(1L)
            },
            onNextMonth = {
                currentMonth = currentMonth.plusMonths(1L)
            }
        )

        Spacer(modifier = Modifier.height(5.dp))


        Divider()


        CalendarGrid(
            month = currentMonth,
            chosenDate = chosenDate,
            onSelectedDate = {
                selectedDate = it
                onSelectedDate(it)
            }
        )
    }
}

@Composable
fun CalendarHeader(
    month:YearMonth,
    onPreviousMonth:() -> Unit,
    onNextMonth:() -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onPreviousMonth()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Arrow Back"
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = month.month.name
            )
            Text(
                text = month.year.toString()
            )
        }


        IconButton(
            onClick = {
                onNextMonth()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Arrow Forward"
            )
        }


    }
}


@Composable
fun CalendarGrid(
    chosenDate: LocalDate?,
    month: YearMonth,
    onSelectedDate:(LocalDate) -> Unit,
    content:@Composable ((List<LocalDate>) -> Unit)? = null
){
    // how many days are in this month
    val monthLength = month.lengthOfMonth()

    // first day so we can determine where month starts
    val firstDayOfWeek = month.atDay(1).dayOfWeek.value - 1 // Wednesday -> 3 - 1 = index 2

    // go through length of month and map every day in list
    val dates = (1..monthLength).map { day -> month.atDay(day) }

    // create new list and for first n indexes (firstDayOfWeek) fill them with null and add valid dates
    val allDates = List<LocalDate?>(firstDayOfWeek) { null } + dates

    val selectedDate:MutableState<LocalDate?> = remember { mutableStateOf(chosenDate) }


    if(content == null){
        LazyVerticalGrid(
            contentPadding = PaddingValues(10.dp),
            columns = GridCells.Fixed(7),
        ) {
            item(span = { GridItemSpan(7) }) {
                DaysIWeek()
            }
            items(allDates){date ->
                DayItem(
                    date = date,
                    selectedDate = selectedDate.value,
                    onSelectedDate = {
                        onSelectedDate(date!!)
                        selectedDate.value = date
                    }
                )
            }
        }
    }else{
        content(dates)
    }


}

@Composable
fun DayItem(
    date:LocalDate?,
    selectedDate:LocalDate?,
    onSelectedDate: () -> Unit
){


    val itemColor:Color = if(selectedDate != null && selectedDate == date){
         Purple
    }else if(date != null && (date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now()))){
        DarkGray
    }else{
        LightBlack
    }

    Text(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(color = itemColor, shape = RoundedCornerShape(4.dp))
            .shouldClick(
                date = date,
                onClick = {
                    onSelectedDate()
                }
            ),
        text = date?.dayOfMonth?.toString() ?: "",
        textAlign = TextAlign.Center
    )
}


@Composable
fun DaysIWeek(){
    val days = listOf(
        DaysInWeek.MONDAY,
        DaysInWeek.TUESDAY,
        DaysInWeek.WEDNESDAY,
        DaysInWeek.THURSDAY,
        DaysInWeek.FRIDAY,
        DaysInWeek.SATURDAY,
        DaysInWeek.SUNDAY,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (day in days){
            Text(
                text = day.day,
                textAlign = TextAlign.Justify,
                fontSize = 14.sp,
                color = day.color
            )
        }
    }
}


fun Modifier.shouldClick(
    date: LocalDate?,
    onClick:() -> Unit
) : Modifier {
    return if(date != null && (date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now()))){
        this.then(Modifier.clickable {
            onClick()
        })
    }else{
        this
    }
}

enum class DaysInWeek(val day:String,val color:Color){
    MONDAY(day = "MON", color = Color.White),
    TUESDAY(day = "TUE", color = Color.White),
    WEDNESDAY(day = "WED", color = Color.White),
    THURSDAY(day = "THU  ", color = Color.White),
    FRIDAY(day = "FRI  ", color = Color.White),
    SATURDAY(day = "SAT", color = Color.Red),
    SUNDAY(day = "SUN", color = Color.Red);
}





