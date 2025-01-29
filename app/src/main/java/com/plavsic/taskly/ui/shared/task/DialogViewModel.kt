package com.plavsic.taskly.ui.shared.task

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.plavsic.taskly.domain.category.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor() : ViewModel() {

    private val _isCategoryDialogVisible = mutableStateOf(false)
    val isCategoryDialogVisible: State<Boolean> get() = _isCategoryDialogVisible

    private val _isTaskDialogVisible = mutableStateOf(false)
    val isTaskDialogVisible: State<Boolean> get() = _isTaskDialogVisible

    private val _selectedDate: MutableState<LocalDate?> = mutableStateOf(null)
    val selectedDate: State<LocalDate?> get() = _selectedDate

    private val _selectedCategory: MutableState<Category?> = mutableStateOf(null)
    val selectedCategory: State<Category?> get() = _selectedCategory

    private val _selectedPriority: MutableState<TaskPriority?> = mutableStateOf(null)
    val selectedPriority: State<TaskPriority?> get() = _selectedPriority


    fun showCategoryDialog() {
        _isCategoryDialogVisible.value = true
    }

    fun hideCategoryDialog() {
        _isCategoryDialogVisible.value = false
    }

    fun showTaskDialog() {
        _isTaskDialogVisible.value = true
    }

    fun hideTaskDialog() {
        _isTaskDialogVisible.value = false
    }

    fun setSelectedDate(date: LocalDate?) {
        _selectedDate.value = date
    }

    fun clearSelectedDate() {
        _selectedDate.value = null
    }

    fun setSelectedCategory(category: Category?) {
        _selectedCategory.value = category
    }

    fun clearSelectedCategory() {
        _selectedCategory.value = null
    }

    fun setSelectedPriority(priority: TaskPriority?) {
        _selectedPriority.value = priority
    }

    fun clearSelectedPriority() {
        _selectedPriority.value = null
    }

}