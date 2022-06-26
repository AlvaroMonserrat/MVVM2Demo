package com.rrat.mvvm2demo.application

import android.app.Application
import com.rrat.mvvm2demo.model.database.FavDishRepository
import com.rrat.mvvm2demo.model.database.FavDishRoomDatabase
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class FavDishApplication : Application(){

    //private val database by lazy { FavDishRoomDatabase.getDatabase(this@FavDishApplication) }
    //val repository by lazy { FavDishRepository(database.favDishDao()) }
}