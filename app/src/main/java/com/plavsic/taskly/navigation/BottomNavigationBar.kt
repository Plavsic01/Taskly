package com.plavsic.taskly.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plavsic.taskly.R
import com.plavsic.taskly.ui.homeScreen.HomeScreen
import com.plavsic.taskly.ui.shared.category.CategoryDialog
import com.plavsic.taskly.ui.shared.task.AddTaskDialog
import com.plavsic.taskly.ui.shared.task.DialogViewModel
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.Purple


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    dialogViewModel: DialogViewModel
) {
    val items = listOf(
        BottomNavigationItem.Home,
        BottomNavigationItem.Calendar,
        BottomNavigationItem.Focus,
        BottomNavigationItem.Profile
    )

    val showTaskDialog by dialogViewModel.isTaskDialogVisible

    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(3f)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple,
                    ),
                    onClick = {
                        dialogViewModel.showTaskDialog()
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .offset(y = (-32).dp)
                        .size(64.dp)

                ) {
                    Image(
                        painter = painterResource(R.drawable.add),
                        contentDescription = "Add Button",
                        modifier = Modifier.size(64.dp).scale(1.5f)
                    )
                }
            }


            TasklyBottomNavigationBar(
                navController = navController,
                items = items
            )
        }
    ) {innerPadding ->
        AddTaskDialog(
            showDialog = showTaskDialog,
            navController = navController,
            dialogViewModel = dialogViewModel
        )

        NavigationHost(
            navController = bottomNavController,
        )
    }
}

@Composable
fun TasklyBottomNavigationBar(
    navController: NavHostController,
    items:List<BottomNavigationItem>
) {

    val selectedNavItem = remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(DarkerGray),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy((-10).dp)
            ) {
            items.take(2).forEachIndexed { index, item ->
                TasklyBottomNavItemImpl(item, selectedNavItem, index)
                }
            }


            Row(
                horizontalArrangement = Arrangement.spacedBy((-10).dp)
            ) {
                items.takeLast(2).forEachIndexed { index, item ->
                    TasklyBottomNavItemImpl(item, selectedNavItem, index,add = 2)
                }
            }
        }
    }
}

@Composable
private fun TasklyBottomNavItemImpl(
    item: BottomNavigationItem,
    selectedNavItem: MutableIntState,
    index: Int,
    add:Int = 0
) {
    TasklyNavigationBarItem(
        label = {
            Text(
                style = MaterialTheme.typography.labelSmall,
                text = item.label,
            )
        },
        icon = {
            Image(
                modifier = Modifier.size(20.dp),
                painter = if (selectedNavItem.intValue == index + add) painterResource(id = item.clickedIcon)
                else
                    painterResource(id = item.icon),
                contentDescription = item.label
            )
        },
        onClick = {
            selectedNavItem.intValue = index + add
            Log.i("BtnClick", "Clicked Btn $index")
        }
    )
}


@Composable
fun TasklyNavigationBarItem(
//    selected:Boolean,
    label: @Composable (() -> Unit),
    icon: @Composable (() -> Unit),
    onClick: () -> Unit
) {
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            interactionSource = NoRippleInteractionSource()
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                icon()
                Spacer(modifier = Modifier.height(5.dp))
                label()
            }
        }
    }


@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationItem.Home.route,
    ) {
        composable(BottomNavigationItem.Home.route) { HomeScreen() }
        composable(BottomNavigationItem.Calendar.route) {  }
        composable(BottomNavigationItem.Focus.route) {  }
        composable(BottomNavigationItem.Profile.route) {  }
    }
}

sealed class BottomNavigationItem(val route: String,
                                  val icon: Int,
                                  val clickedIcon:Int,
                                  val label: String) {
    data object Home : BottomNavigationItem("home",R.drawable.home_outline, R.drawable.home, "Home")
    data object Calendar : BottomNavigationItem("calendar",R.drawable.calendar_outline, R.drawable.calendar, "Calendar")
    data object Focus : BottomNavigationItem("focus",R.drawable.clock_outline,R.drawable.clock,"Focus")
    data object Profile : BottomNavigationItem("profile",R.drawable.user_outline,R.drawable.user_outline, "Profile")
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun BottomNavigationBarPreview() {
//    BottomNavigationBar()
//}