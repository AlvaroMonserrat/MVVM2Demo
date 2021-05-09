package com.rrat.mvvm2demo.model.database

import androidx.annotation.WorkerThread
import com.rrat.mvvm2demo.model.entities.FavDish

class FavDishRepository(private val favDishDAO: FavDishDAO) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish){
        favDishDAO.insertFavDishDetails(favDish)
    }

}