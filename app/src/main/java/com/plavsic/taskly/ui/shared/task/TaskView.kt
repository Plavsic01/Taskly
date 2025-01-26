package com.plavsic.taskly.ui.shared.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.plavsic.taskly.domain.task.model.Task
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.utils.longToULong

@Composable
fun TaskView(
    modifier: Modifier,
    task:Task
){
    Column(
        modifier = modifier
            .background(color = DarkerGray, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
        ,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = task.title,
            textAlign = TextAlign.Left,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.date.toString(),
                fontSize = 14.sp
            )


            Row {
                IconWithText(
                    modifier = Modifier
                        .background(Color(value = longToULong(task.category!!.color)),
                            shape = RoundedCornerShape(4.dp)),
                    image = task.category.image.toInt(),
                    text = task.category.name
                )

                Spacer(Modifier.width(10.dp))

                IconWithText(
                    modifier = Modifier
                        .border(width = 1.dp, color = Purple, shape = RoundedCornerShape(4.dp)),
                    image = task.priority!!.imageName,
                    text = task.priority.number.toString(),
                )
            }


        }
    }
}


@Composable
fun IconWithText(
    modifier: Modifier,
    image:Int,
    text:String,

){
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(20.dp,20.dp),
            painter = painterResource(image),
            contentDescription = text,
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(text)
    }
}

