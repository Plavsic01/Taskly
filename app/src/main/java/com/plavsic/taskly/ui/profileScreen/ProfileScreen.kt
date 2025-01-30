package com.plavsic.taskly.ui.profileScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.Gray


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier
            .systemBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                ),
                title = {
                    Text(
                        text = "Profile",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                },
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            // User Photo

            AsyncImage(
                modifier = Modifier
                    .size(85.dp)
                    .clip(shape = CircleShape),
                model = profileViewModel.getUserProfilePicture(),
                contentDescription = "Avatar"
            )

            // E-mail

            Text(
                modifier = Modifier
                    .padding(vertical = 20.dp),
                text = "andrejplavsic3@gmail.com",
                fontSize = 18.sp
            )


            // Tasks Left and Done
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 35.dp, vertical = 17.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = DarkerGray,
                        contentColor = Color.White
                    ),
                    onClick = {}
                ) {
                    Text(
                        text = "10 tasks left"
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                TextButton(
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 35.dp, vertical = 17.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = DarkerGray,
                        contentColor = Color.White
                    ),
                    onClick = {}
                ) {
                    Text(
                        text = "5 tasks done"
                    )
                }
            }

        }
    }


}













//Button(
//onClick = {
//    profileViewModel.logOut()
//}
//) {
//    Text(
//        text = "Log out"
//    )
//}