package com.plavsic.taskly.ui.onboardingScreen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.plavsic.taskly.R
import com.plavsic.taskly.navigation.NavigationGraph
import com.plavsic.taskly.ui.theme.Purple
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(navController: NavHostController) {
    val pageState = rememberPagerState(pageCount = {
        3
    })

    HorizontalPager(state = pageState, userScrollEnabled = false) { page ->
        OnboardingView(navController = navController,state = pageState,currPage = page)
    }
}


@Composable
fun OnboardingView(
    navController: NavHostController,
    state:PagerState,
    currPage:Int,
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Image(
            painter = painterResource(id = onboardingData[currPage].imageName),
            contentDescription = onboardingData[currPage].imageDescription,
            modifier = Modifier
                .width(213.dp)
                .height(277.dp)
        )


        Row(
            modifier = Modifier.width(95.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            onboardingData.forEachIndexed {index, _ ->
                PageIndicatorView(isCurrPage = index == currPage)
            }
        }

        TextSectionView(currPage)

        Spacer(modifier = Modifier.height(16.dp))

        NavigationView(navController = navController,state = state)

    }
}

@Composable
private fun TextSectionView(currPage: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = onboardingData[currPage].title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = onboardingData[currPage].subtitle,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall

        )
    }
}


@Composable
private fun NavigationView(
    navController: NavHostController,
    state: PagerState
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        TextButton(
            onClick = {
                if (state.currentPage > 0)
                    scope.launch {
                        state.animateScrollToPage(state.currentPage - 1)
                    }
            }
        ) {
            Text(
                text = "BACK",
                color = Color.Gray
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                // This means its on GET STARTED (Last page of onboarding pages)
                if(state.currentPage == onboardingData.size - 1){
                    navController.navigate(NavigationGraph.StartScreen.route)
                }

                if (state.currentPage < onboardingData.size - 1) {
                    scope.launch {
                        state.animateScrollToPage(state.currentPage + 1)
                    }
                }
            }) {
            Text(
                text = if (state.currentPage != 2) "NEXT" else "GET STARTED",
            )
        }
    }
}


@Composable
fun PageIndicatorView(isCurrPage:Boolean) {
    Box(modifier = Modifier
        .size(27.dp,4.dp)
        .background(
            color = if (isCurrPage) Color.White else Color.Gray,
            shape = RoundedCornerShape(4.dp)
        )
    )
}


data class OnboardingInfo(
    val imageName:Int,
    val imageDescription:String,
    val title:String,
    val subtitle:String
)


val onboardingData = listOf(
    OnboardingInfo(
        R.drawable.manage_time,
        "Time Manage Icon",
        "Manage your tasks",
        "You can easily manage all of your daily\n" +
                " tasks in Taskly for free"),

    OnboardingInfo(
        R.drawable.daily_routine,
        "Daily Routine Icon",
        "Create daily routine",
        "In Taskly you can create your\n" +
                "personalized routine to stay productive"),

    OnboardingInfo(
        R.drawable.organize_tasks,
        "Organize Tasks Icon",
        "Organize your tasks",
        "You can organize your daily tasks by\n" +
                "your tasks into separate categories"),
)




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingScreenPreview() {
    val pageState = rememberPagerState(pageCount = {
        3
    })
//    OnboardingView(state = pageState, currPage = 0)
}
