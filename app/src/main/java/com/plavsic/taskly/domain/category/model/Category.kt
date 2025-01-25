package com.plavsic.taskly.domain.category.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Default values are for Firestore because if there are no documents he has to put default values
@Parcelize
data class Category(
    val id: Long = 0,
    val image:Long = 0,
    val name:String = "",
    val color:Long = 0
) : Parcelable
