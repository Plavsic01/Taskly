package com.plavsic.taskly.domain.category.repository

import com.plavsic.taskly.domain.category.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll() : Flow<List<Category>>
    suspend fun createCategory(category: Category)
    suspend fun deleteCategory()
}