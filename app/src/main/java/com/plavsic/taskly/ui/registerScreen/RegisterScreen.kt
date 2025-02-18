package com.plavsic.taskly.ui.registerScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.AuthResult
import com.plavsic.taskly.core.Response
import com.plavsic.taskly.navigation.NavigationGraph
import com.plavsic.taskly.ui.shared.auth.InputSection
import com.plavsic.taskly.ui.shared.common.TasklyButton
import com.plavsic.taskly.ui.theme.Gray
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.ui.theme.WhiteWithOpacity87
import kotlinx.coroutines.flow.MutableSharedFlow




@Composable
fun RegisterScreen(
    navController: NavHostController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val isEnabled = remember { mutableStateOf(true) }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

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
            text = "Register",
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

            Spacer(modifier = Modifier.height(16.dp))

            InputSection(state = confirmPassword,
                text = "Confirm Password",
                isPassword = true,
                placeholder = "••••••••••••")

        }

        Column {
            Column {
                TasklyButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        if(password.value == confirmPassword.value){
                            registerViewModel.register(email.value, password.value)
                        }else {
                            Toast.makeText(context,"Passwords must match",Toast.LENGTH_SHORT).show()
                        }
                    },
                    text = "Register",
                    enabled = isEnabled.value,
                    containerColor = Purple,
                    contentColor = Color.White
                )
            }
        }



        Row {
            Text(
                fontSize = 12.sp,
                text = "Already have an account? ",
                color = Gray
            )
            Text(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigate(NavigationGraph.LoginScreen.route)
                    },
                fontSize = 12.sp,
                text = "Login",
                color = WhiteWithOpacity87
            )
        }
    }

    RegisterState(
        flow = registerViewModel.registerFlow,
        onLoading = {
            isEnabled.value = false
        },
        onSuccess = {
            registerViewModel.createUserDocument(
                onSuccess = {
                    navController.navigate(NavigationGraph.MainScreen.route) {
                        launchSingleTop = true
                        popUpTo(NavigationGraph.StartScreen.route) { inclusive = true }
                    }
                },
                onFailure = {}
            )
        },
        onError = { err ->
            isEnabled.value = true
            Toast.makeText(context,err,Toast.LENGTH_SHORT).show()
        }

    )

}


@Composable
fun RegisterState(
    flow: MutableSharedFlow<Response<AuthResult>>,
    onLoading: () -> Unit,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    if (isLoading.value) {
        onLoading()
    }
    LaunchedEffect (Unit) {
        flow.collect {
            when (it) {
                is Response.Loading -> {
                    isLoading.value = true
                }

                is Response.Success -> {
                    isLoading.value = false
                    onSuccess()
                }

                is Response.Error -> {
                    isLoading.value = false
                    onError(it.message)
                }


            }
        }
    }
}

