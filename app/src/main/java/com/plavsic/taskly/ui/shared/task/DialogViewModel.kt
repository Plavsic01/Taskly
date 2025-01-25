package com.plavsic.taskly.ui.shared.task

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor() : ViewModel() {

    private val _isCategoryDialogVisible = mutableStateOf(false)
    val isCategoryDialogVisible: State<Boolean> get() = _isCategoryDialogVisible

    private val _isTaskDialogVisible = mutableStateOf(false)
    val isTaskDialogVisible: State<Boolean> get() = _isTaskDialogVisible

    init {
        Log.d("DialogViewModel", "Instance created!")
    }

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
}