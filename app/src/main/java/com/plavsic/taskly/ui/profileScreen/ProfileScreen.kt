package com.plavsic.taskly.ui.profileScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.plavsic.taskly.R
import com.plavsic.taskly.domain.auth.model.UserInfo
import com.plavsic.taskly.ui.shared.task.TaskState
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.LightWhite


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel
) {
    val authProvider by profileViewModel.authProvider.collectAsStateWithLifecycle()
    val userInfo = profileViewModel.userInfo.collectAsStateWithLifecycle()
    var userData by remember { mutableStateOf(UserInfo("","")) }

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
                model = userData.image,
                contentDescription = "Avatar"
            )

            // E-mail

            Text(
                modifier = Modifier
                    .padding(vertical = 20.dp),
                text = userData.username,
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


            Spacer(modifier = Modifier.height(20.dp))

            // Account

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 30.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Account",
                    color = LightWhite,
                    fontSize = 14.sp,
                )

                if(authProvider == Provider.EMAIL){
                    AccountItem(
                        image = R.drawable.user_outline,
                        title = "Change account name",
                        onClick = {}
                    )


                    AccountItem(
                        image = R.drawable.key,
                        title = "Change account password",
                        onClick = {

                        }
                    )

                    AccountItem(
                        image = R.drawable.camera,
                        title = "Change account image",
                        onClick = {
//                            profileViewModel.updateProfilePicture("https://cdn-icons-png.flaticon.com/512/149/149072.png")
                        }
                    )
                }



                AccountItem(
                    image = R.drawable.logout,
                    title = "Log out",
                    showArrow = true,
                    onClick = {
                        profileViewModel.logOut()
                    }
                )
            }
        }
    }

    TaskState(
        state = userInfo,
        onLoading = {},
        onSuccess = {
            val data = it as UserInfo
            userData = data
            Log.i("USERINFO",data.toString())
        },
        onError = {}
    )
}

@Composable
private fun AccountItem(
    image: Int,
    title:String,
    showArrow:Boolean = true,
    onClick:() -> Unit

) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {

            Image(
                painter = painterResource(id = image),
                contentDescription = "Camera"
            )

            Text(
                modifier = Modifier
                    .padding(start = 10.dp),
                text = title
            )
        }
        if(showArrow){
            IconButton(onClick = {
                onClick()
            }) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Left Arrow"
                )
            }
        }
    }
}


enum class Provider {
    DEFAULT,
    GOOGLE,
    EMAIL;
}