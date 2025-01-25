package com.plavsic.taskly.domain.category.repository


import com.plavsic.taskly.data.database.dao.CategoryDAO
import com.plavsic.taskly.data.database.entities.CategoryEntity
import com.plavsic.taskly.data.database.repository.CategoryRepository
import com.plavsic.taskly.domain.category.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDAO: CategoryDAO
) : CategoryRepository {

    override fun getAll(): Flow<List<Category>> {
        return categoryDAO.getAll().map { entityList ->
            entityList.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun createCategory(category: Category) {
        categoryDAO.insertCategory(category.toEntity())
    }

    override suspend fun deleteCategory() {
        TODO("Not yet implemented")
    }
}


// Convert Entity to Model
fun CategoryEntity.toDomain(): Category {
    return Category(
        id = this.id,
        image = this.image,
        name = this.name,
        color = this.color
    )
}

// Convert Model to Entity
fun Category.toEntity() : CategoryEntity {
    return CategoryEntity(
        image = this.image,
        name = this.name,
        color = this.color
    )
}