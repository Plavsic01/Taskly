package com.plavsic.taskly.ui.startScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.ui.theme.WhiteWithOpacity

@Composable
fun StartScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Column {
            Text(
                text = "Welcome to Taskly",
                style =  MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Please login to your account or create\n new account to continue",
                style =  MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                color = WhiteWithOpacity
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {

            TasklyButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    println("TEST") // TESTING
                },
                text = "LOGIN",
                containerColor = Purple,
                contentColor = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            TasklyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp,Purple, shape = RoundedCornerShape(4.dp)),
                onClick = {},
                text = "CREATE ACCOUNT",
                containerColor = Background,
                contentColor = Color.White
            )
        }
    }
}

@Composable
fun TasklyButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text:String,
    containerColor:Color,
    contentColor:Color
    ) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(4.dp),
        onClick = {
            onClick()
        }){
        Text(
            text = text
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    StartScreen(navController)
}
