package com.plavsic.taskly.di

import android.content.Context
import androidx.room.Room
import com.plavsic.taskly.data.database.CategoryDatabase
import com.plavsic.taskly.data.database.dao.CategoryDAO
import com.plavsic.taskly.data.database.repository.CategoryRepository
import com.plavsic.taskly.domain.category.repository.CategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCategoryDatabase(
        @ApplicationContext appContext:Context
    ) : CategoryDatabase {
        return Room.databaseBuilder(
            appContext,
            CategoryDatabase::class.java,
            "categories-db"
        )
            .createFromAsset("database/categories.db")
            .build()
    }


    @Provides
    fun provideCategoryDAO(database: CategoryDatabase) : CategoryDAO {
        return database.CategoryDAO()
    }

    @Provides
    fun provideCategoryRepository(categoryDAO: CategoryDAO) : CategoryRepository {
        return CategoryRepositoryImpl(categoryDAO)
    }

}