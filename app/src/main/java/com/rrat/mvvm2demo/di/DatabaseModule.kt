package com.rrat.mvvm2demo.di

import android.app.Application
import com.rrat.mvvm2demo.model.database.FavDishDAO
import com.rrat.mvvm2demo.model.database.FavDishRepository
import com.rrat.mvvm2demo.model.database.FavDishRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFavDishDao(app: Application) : FavDishDAO{
        return FavDishRoomDatabase.getDatabase(app.applicationContext).favDishDao()
    }

    @Provides
    @Singleton
    fun provideRepository(favDishDAO: FavDishDAO) : FavDishRepository
    {
        return FavDishRepository(favDishDAO)
    }
}