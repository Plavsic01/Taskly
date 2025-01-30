package com.plavsic.taskly.ui.categoryScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.plavsic.taskly.domain.category.model.CategoryIcon
import com.plavsic.taskly.ui.shared.category.CategoryItem
import com.plavsic.taskly.ui.shared.common.Divider
import com.plavsic.taskly.ui.theme.DarkerGray

@Composable
fun CategoryLibraryDialog(
    showDialog:MutableState<Boolean>,
    onSelectedIcon:(CategoryIcon)-> Unit
) {

    val selectedIcon = remember { mutableStateOf(CategoryIcon.DEFAULT) }

    // Icons
    val icons = listOf(
        CategoryIcon.IDEA,
        CategoryIcon.TV,
        CategoryIcon.TRAVEL,
        CategoryIcon.SHOPPING,
        CategoryIcon.CAR,
        CategoryIcon.GAMING,
        CategoryIcon.DIY,
        CategoryIcon.MEDICINE,
        CategoryIcon.FOOD,
    )


    if(showDialog.value){
        Dialog(onDismissRequest = {
            showDialog.value = false
        }){
            Card(
                modifier = Modifier
                    .height(420.dp)
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
                        text = "Choose Icon",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Divider()
                }
                LazyVerticalGrid(
                    modifier = Modifier
                        .weight(1f),
                    contentPadding = PaddingValues(20.dp),
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(icons){
                        CategoryItem(
                            icon = it.resId,
                            contentDescription = "Icon ${it.name}",
                            onClick = {
                                selectedIcon.value = it
                                showDialog.value = false
                                onSelectedIcon(selectedIcon.value)
                            }
                        )
                    }
                }
            }
        }
    }
}

