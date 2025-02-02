package com.plavsic.taskly.ui.profileScreen


import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.plavsic.taskly.R
import com.plavsic.taskly.domain.auth.model.UserInfo
import com.plavsic.taskly.navigation.NavigationGraph
import com.plavsic.taskly.ui.shared.common.DualActionButtons
import com.plavsic.taskly.ui.shared.common.TasklyTextField
import com.plavsic.taskly.ui.shared.task.TaskState
import com.plavsic.taskly.ui.taskScreen.TaskDialog
import com.plavsic.taskly.ui.theme.Background
import com.plavsic.taskly.ui.theme.LightWhite


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController:NavHostController,
    profileViewModel: ProfileViewModel
) {

    val context = LocalContext.current

    val authProvider by profileViewModel.authProvider.collectAsStateWithLifecycle()
    val userInfo = profileViewModel.userInfo.collectAsStateWithLifecycle()
    var userData by remember { mutableStateOf(UserInfo()) }

    val showAccountDialog = remember { mutableStateOf(false) }
    val showChangePasswordDialog = remember { mutableStateOf(false) }

    val accountName = remember { mutableStateOf("") }
    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }


    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            uri ->
        if(uri != null){

            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION

            try {
                context.contentResolver.takePersistableUriPermission(uri,flag)
            }catch (e:SecurityException){
                e.printStackTrace()
            }

            profileViewModel.updateProfilePicture(uri.toString())
        }
    }


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

            Log.i("USERDATA",userData.image)

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
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                TextButton(
//                    shape = RoundedCornerShape(6.dp),
//                    contentPadding = PaddingValues(horizontal = 35.dp, vertical = 17.dp),
//                    colors = ButtonDefaults.textButtonColors(
//                        containerColor = DarkerGray,
//                        contentColor = Color.White
//                    ),
//                    onClick = {}
//                ) {
//                    Text(
//                        text = "${userData.tasksLeft} tasks left"
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(20.dp))
//
//                TextButton(
//                    shape = RoundedCornerShape(6.dp),
//                    contentPadding = PaddingValues(horizontal = 35.dp, vertical = 17.dp),
//                    colors = ButtonDefaults.textButtonColors(
//                        containerColor = DarkerGray,
//                        contentColor = Color.White
//                    ),
//                    onClick = {}
//                ) {
//                    Text(
//                        text = "${userData.tasksDone} tasks done"
//                    )
//                }
//            }


//            Spacer(modifier = Modifier.height(20.dp))

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


                AccountItem(
                    image = R.drawable.user_outline,
                    title = "Change account name",
                    onClick = {
                        showAccountDialog.value = true
                    }
                )

                if(authProvider == Provider.EMAIL) {
                    AccountItem(
                        image = R.drawable.key,
                        title = "Change account password",
                        onClick = {
                            showChangePasswordDialog.value = true
                        }
                    )
                }

                AccountItem(
                    image = R.drawable.camera,
                    title = "Change account image",
                    onClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                )

                AccountItem(
                    image = R.drawable.logout,
                    title = "Log out",
                    showArrow = true,
                    onClick = {
                        profileViewModel.logOut()
                        navController.navigate(NavigationGraph.StartScreen.route){
                            popUpTo(NavigationGraph.StartScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }


                TaskDialog(
                    showDialog = showAccountDialog,
                    height = 230.dp,
                    title = "Change account name",
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceAround
                        ) {

                            Spacer(modifier = Modifier.height(10.dp))

                            TasklyTextField(
                                state = accountName,
                                placeholder = userData.username,
                                onValueChange = {
                                    accountName.value = it
                                }
                            )

                            DualActionButtons(
                                modifier = Modifier.weight(1f),
                                btn1Text = "Cancel",
                                btn2Text = "Edit",
                                onClickBtn1 = {
                                    showAccountDialog.value = false
                                    accountName.value = ""
                                },
                                onClickBtn2 = {
                                    profileViewModel.updateUsername(accountName.value)
                                    showAccountDialog.value = false
                                    accountName.value = ""
                                }
                            )
                        }
                    }
                )

                TaskDialog(
                    showDialog = showChangePasswordDialog,
                    title = "Change account password",
                    height = 300.dp,
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {

                            Spacer(modifier = Modifier.height(10.dp))

                            TasklyTextField(
                                label = {
                                    Text(
                                        text = "Enter current password",
                                        fontSize = 12.sp
                                    )
                                },
                             state = currentPassword,
                             isPassword = true,
                             onValueChange = {
                                currentPassword.value = it
                                }
                            )

                            TasklyTextField(
                                label = {
                                    Text(
                                        text = "Enter new password",
                                        fontSize = 12.sp
                                    )
                                },
                                state = newPassword,
                                isPassword = true,
                                onValueChange = {
                                    newPassword.value = it
                                }
                            )

                            DualActionButtons(
                                modifier = Modifier.weight(1f),
                                btn1Text = "Cancel",
                                btn2Text = "Edit",
                                enabled = currentPassword.value.length >= 6 &&
                                        newPassword.value.length >= 6,
                                onClickBtn1 = {
                                    showChangePasswordDialog.value = false
                                    resetState(currentPassword, newPassword)
                                },
                                onClickBtn2 = {
                                    // Change password
                                    profileViewModel.reauthenticateAndChangePassword(
                                        currentPassword = currentPassword.value,
                                        newPassword = newPassword.value,
                                        onSuccess = {
                                            resetState(currentPassword, newPassword)
                                            Toast.makeText(context,"Password successfully changed!",
                                                Toast.LENGTH_SHORT).show()
                                        },
                                        onFailure = {
                                            Toast.makeText(context,it.message,
                                                Toast.LENGTH_LONG).show()
                                        }
                                    )
                                    showChangePasswordDialog.value = false
                                }
                            )
                        }
                    }
                )

            }
        }

    TaskState(
        state = userInfo,
        onLoading = {},
        onSuccess = {
            val data = it as UserInfo
            userData = data
        },
        onError = {}
    )
}

private fun resetState(
    currentPassword: MutableState<String>,
    newPassword: MutableState<String>
) {
    currentPassword.value = ""
    newPassword.value = ""
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