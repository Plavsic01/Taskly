package com.plavsic.taskly.ui.categoryScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.plavsic.taskly.domain.category.model.Category
import com.plavsic.taskly.domain.category.model.CategoryIcon
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
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.Gray
import com.plavsic.taskly.ui.theme.WhiteWithOpacity21
import com.plavsic.taskly.ui.theme.WhiteWithOpacity87
import com.plavsic.taskly.utils.conversion.uLongToLong


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
    var selectedIcon by remember { mutableStateOf(CategoryIcon.DEFAULT) }
    var selectedColor by remember { mutableStateOf<Color?>(null) }

    val showLibraryDialog = remember { mutableStateOf(false) }


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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TasklyButton(
                    onClick = {
                        // Select Icon from library
                        showLibraryDialog.value = true
                    },
                    text = "Chose icon from library",
                    contentColor = WhiteWithOpacity87,
                    containerColor = WhiteWithOpacity21
                )

                if(selectedIcon.name != "DEFAULT"){
                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        modifier = Modifier
                            .size(40.dp),
                        painter = painterResource(selectedIcon.resId),
                        contentDescription = "Icon ${selectedIcon.name}"
                    )
                }
            }


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
                    Button(
                        modifier = Modifier.size(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(selectedColor == it) DarkerGray else it
                        ),
                        shape = CircleShape,
                        onClick = {
                            selectedColor = it
                        }
                    ){}
                }
            }
        }

        DualActionButtons(
            modifier = Modifier,
            enabled = categoryName.value.isNotEmpty() && selectedIcon.name != "DEFAULT" && selectedColor != null,
            onClickBtn1 = {
                goBack(dialogViewModel, navController)
            },
            onClickBtn2 = {
                // Create Category
                categoryViewModel.createCategory(
                    category = Category(
                        id = 0,
                        image = selectedIcon.name,
                        name = categoryName.value,
                        color = uLongToLong(selectedColor!!.value)
                    )
                )
                goBack(dialogViewModel, navController)
            },
            btn1Text = "Cancel",
            btn2Text = "Create Category"
        )

    }

    CategoryLibraryDialog(
        showDialog = showLibraryDialog,
        onSelectedIcon = {
            selectedIcon = it
        }
    )

}


private fun goBack(
    dialogViewModel: DialogViewModel,
    navController: NavHostController
) {
    dialogViewModel.clearSelectedCategory()
    dialogViewModel.showCategoryDialog()
    dialogViewModel.showTaskDialog()
    navController.popBackStack()
}

