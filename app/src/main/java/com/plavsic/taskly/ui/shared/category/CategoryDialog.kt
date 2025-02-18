package com.plavsic.taskly.ui.shared.category


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.plavsic.taskly.domain.category.model.Category
import com.plavsic.taskly.domain.category.model.CategoryIcon
import com.plavsic.taskly.navigation.NavigationGraph
import com.plavsic.taskly.ui.categoryScreen.CategoryViewModel
import com.plavsic.taskly.ui.shared.common.Divider
import com.plavsic.taskly.ui.shared.common.TasklyButton
import com.plavsic.taskly.ui.shared.task.DialogViewModel
import com.plavsic.taskly.ui.theme.CategoryTurquoise
import com.plavsic.taskly.ui.theme.DarkerGray
import com.plavsic.taskly.ui.theme.Purple
import com.plavsic.taskly.ui.theme.WhiteWithOpacity87
import com.plavsic.taskly.utils.conversion.longToULong
import com.plavsic.taskly.utils.conversion.uLongToLong

@Composable
fun CategoryDialog(
    navController:NavHostController,
    isForEdit:Boolean = false,
    onEdit:() -> Unit = {},
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel
){

    val showCategoryDialog by dialogViewModel.isCategoryDialogVisible

    val selectedCategory by dialogViewModel.selectedCategory

    val categories by categoryViewModel.categories.collectAsStateWithLifecycle()
    val addCategory = Category(-1, "ADD", "Create New", uLongToLong(CategoryTurquoise.value))
    val categoriesWithAddButton = categories + addCategory


    if(showCategoryDialog){
        Dialog(onDismissRequest = {
            dialogViewModel.hideCategoryDialog()
            if(!isForEdit){
                dialogViewModel.clearSelectedCategory()
            }
        }){
            Card(
                modifier = Modifier
                    .height(550.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = DarkerGray
                )
            ){
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp)
                            .fillMaxWidth(),
                        text = "Choose Category",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Divider()
                }
                LazyVerticalGrid(
                    modifier = Modifier
                        .weight(1f),
                    contentPadding = PaddingValues(10.dp),
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(categoriesWithAddButton){
                        CategoryItem(
                            modifier = Modifier.size(40.dp),
                            isSelected = it == selectedCategory,
                            name = it.name,
                            backgroundColor = it.color,
                            icon = CategoryIcon.fromName(it.image)!!.resId,
                            contentDescription = it.name,
                            onClick = {
                                if(it.name != "Create New") {
                                    dialogViewModel.setSelectedCategory(it)
                                }else{
                                    dialogViewModel.hideCategoryDialog()
                                    dialogViewModel.hideTaskDialog()
                                    navController.navigate(NavigationGraph.CategoryScreen.route)
                                }
                            }
                        )
                    }
                }

                TasklyButton(
                    enabled = selectedCategory != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                    ,
                    onClick = {
                        dialogViewModel.hideCategoryDialog()
                        onEdit()
                    },
                    text = if(!isForEdit) "Add Category" else "Edit Category",
                    containerColor = Purple,
                    contentColor = Color.White
                )
            }
        }
    }
}


@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    isSelected:Boolean = false,
    name:String = "",
    backgroundColor:Long? = null,
    icon:Int,
    contentDescription:String,
    onClick:() -> Unit
){

    val priorityColor: Color = if(isSelected){
        Purple
    }else{
        DarkerGray
    }

    Column(
        modifier = Modifier
            .width(64.dp)
            .height(90.dp)
            .background(color = priorityColor)
            .clickable {
                onClick()
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
                .background(color = Color(
                    value = longToULong(backgroundColor ?: Color.Transparent.value.toLong())),
                    shape = RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = modifier,
                painter = painterResource(icon),
                contentDescription = contentDescription,
            )
        }

        Text(
            text = name,
            fontSize = 12.sp,
            color = WhiteWithOpacity87
        )
    }
}
