package com.plavsic.taskly.ui.shared.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.ui.calendarScreen.displayDayOfWeek
import com.plavsic.taskly.ui.theme.Black
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.Purple
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HorizontalCalendarView(
    currentMonth:MutableState<YearMonth>,
    selectedDate:MutableState<LocalDate>,
    tasks:List<Task>

) {
    Column(
        modifier = Modifier
            .padding()
            .background(color = DarkerGray)
    ) {
        CalendarHeader(
            month = currentMonth.value,
            onNextMonth = {
                currentMonth.value = currentMonth.value.plusMonths(1)
            },
            onPreviousMonth = {
                currentMonth.value = currentMonth.value.minusMonths(1)
            }
        )

        CalendarGrid(
            chosenDate = null,
            month = currentMonth.value,
            onSelectedDate = {},
            content = { dates ->
                LazyHorizontalGrid(
                    modifier = Modifier
                        .height(70.dp),
                    contentPadding = PaddingValues(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    rows = GridCells.Fixed(1),
                ) {
                    items(dates){date ->
                        Column(
                            modifier = Modifier
                                .clickable {
                                    selectedDate.value = date
                                }
                                .width(50.dp)
                                .background(color = if(selectedDate.value == date) Purple else Black, shape = RoundedCornerShape(4.dp)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = date.dayOfWeek.getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.ENGLISH).uppercase(
                                    Locale.ROOT),
                                color = displayDayOfWeek(date.dayOfWeek.name)
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString()
                                )

                                if(tasks.any { task ->
                                        task.date?.toLocalDate() == date
                                    }){
                                    Box(
                                        modifier = Modifier
                                            .padding(bottom = 4.dp)
                                            .size(4.dp)
                                            .background(color = Purple, shape = CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}