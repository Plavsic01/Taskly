package com.plavsic.taskly.ui.categoryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.plavsic.taskly.domain.category.model.Category
import com.plavsic.taskly.ui.shared.common.DualActionButtons
import com.plavsic.taskly.ui.shared.common.TasklyButton
import com.plavsic.taskly.ui.shared.common.TasklyTextField
import com.plavsic.taskly.ui.shared.task.DialogViewModel
import com.plavsic.taskly.ui.theme.CategoryBlue
import com.plavsic.taskly.ui.theme.CategoryDarkBlue
import com.plavsic.taskly.ui.theme.CategoryDarkPink
import com.plavsic.taskly.ui.theme.CategoryLightBlue
import com.plavsic.taskly.ui.theme.CategoryLightBrown
import com.plavsic.taskly.ui.theme.CategoryLightGreen
import com.plavsic.taskly.ui.theme.CategoryLighterGreen
import com.plavsic.taskly.ui.theme.CategoryOrange
import com.plavsic.taskly.ui.theme.CategoryPink
import com.plavsic.taskly.ui.theme.CategoryTurquoise
import com.plavsic.taskly.ui.theme.CategoryYellow
import com.plavsic.taskly.ui.theme.WhiteWithOpacity21
import com.plavsic.taskly.ui.theme.WhiteWithOpacity87
import com.plavsic.taskly.utils.uLongToLong


@Composable
fun CategoryScreen(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel
){
    val colors = listOf(
        CategoryYellow,
        CategoryOrange,
        CategoryLightBlue,
        CategoryLighterGreen,
        CategoryDarkBlue,
        CategoryDarkPink,
        CategoryPink,
        CategoryLightGreen,
        CategoryBlue,
        CategoryLightBrown,
        CategoryTurquoise
    )

    val categoryName = remember { mutableStateOf("") }
    val selectedIcon by remember { mutableLongStateOf(2131099652) }
    var selectedColor by remember { mutableStateOf<Color?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 30.dp),
                text = "Create new category",
                fontSize = 20.sp
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp),
                text = "Category name:"
            )

            TasklyTextField(
                state = categoryName,
                placeholder = "Category name"
            ) {
                categoryName.value = it
            }

            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp),
                text = "Category icon:"
            )

            TasklyButton(
                onClick = {},
                text = "Chose icon from library",
                contentColor = WhiteWithOpacity87,
                containerColor = WhiteWithOpacity21
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 10.dp),
                text = "Category color:"
            )

            LazyHorizontalGrid(
                modifier = Modifier.height(50.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                rows = GridCells.Fixed(1)) {
                items(colors) {
                    Box(
                        modifier = Modifier
                            .background(color = it, shape = CircleShape)
                            .size(50.dp)
                            .clickable {
                                selectedColor = it
                            }
                    )
                }
            }
        }

        DualActionButtons(
            onClickBtn1 = {
                dialogViewModel.clearSelectedCategory()
                dialogViewModel.showCategoryDialog()
                dialogViewModel.showTaskDialog()
                navController.popBackStack()
            },
            onClickBtn2 = {
                // Create Category
                categoryViewModel.createCategory(
                    category = Category(
                        id = 0,
                        image = selectedIcon,
                        name = categoryName.value,
                        color = uLongToLong(selectedColor!!.value)
                    )
                )
            },
            btn1Text = "Cancel",
            btn2Text = "Create Category"
        )

    }

}

