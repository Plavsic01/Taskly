package com.plavsic.taskly.ui.loginScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.AuthResult
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.navigation.NavigationGraph
import com.plavsic.taskly.ui.shared.auth.AuthenticationButton
import com.plavsic.taskly.ui.shared.auth.InputSection
import com.plavsic.taskly.ui.shared.auth.SeparatorLine
import com.plavsic.taskly.ui.shared.common.TasklyButton
import com.plavsic.taskly.ui.theme.Gray
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.ui.theme.WhiteWithOpacity87
import kotlinx.coroutines.flow.MutableSharedFlow


@Composable
fun LoginScreen(
    navController:NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {

    val isEnabled = remember { mutableStateOf(true) }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 20.dp),
            text = "Login",
            style = MaterialTheme.typography.titleLarge

        )

        Column {
            InputSection(
                state = email,
                text = "Email",
                isEmail = true,
                placeholder = "Enter your Email"
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputSection(
                state = password,
                text = "Password",
                isPassword = true,
                placeholder = "••••••••••••"
            )

        }

        Column {
            Column {
                TasklyButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        loginViewModel.login(email.value,password.value)
                    },
                    enabled = isEnabled.value,
                    text = "Login",
                    containerColor = Purple,
                    contentColor = Color.White
                )
                SeparatorLine()
            }

            AuthenticationButton(text = "Login with Google") { credential ->
                loginViewModel.loginWithGoogle(credential)
            }
        }


        Row {
            Text(
                fontSize = 12.sp,
                text = "Don't have an account?",
                color = Gray
            )
            Text(
                fontSize = 12.sp,
                text = "Register",
                color = WhiteWithOpacity87
            )
        }
    }

    LoginState(
        flow = loginViewModel.loginFlow,
        onLoading = {
            isEnabled.value = false
        },
        onSuccess = {
            navController.navigate(NavigationGraph.HomeScreen.route)
        },
        onError = {
            Log.i("Error","Error Occurred")
        }

    )

}



@Composable
fun LoginState(
    flow: MutableSharedFlow<Response<AuthResult>>,
    onLoading: () -> Unit,
    onSuccess: () -> Unit,
    onError: () -> Unit

) {
    val isLoading = remember { mutableStateOf(false) }
    if (isLoading.value) {
        onLoading()
    }
    LaunchedEffect (Unit) {
        flow.collect {
            when (it) {
                is Response.Loading -> {
                    Log.i("Login state -> ", "Loading")
                    isLoading.value = true
                }

                is Response.Error -> {
                    it.message
                    Log.e("Login state -> ", it.message)
                    isLoading.value = false
                    onError()
                }

                is Response.Success -> {
                    Log.i("Login",it.data.user!!.email.toString())
                    Log.i("Login state -> ", "Success")
                    isLoading.value = false
                    onSuccess()
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview(){
//    LoginScreen()
}