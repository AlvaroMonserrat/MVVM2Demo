package com.rrat.mvvm2demo.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.rrat.mvvm2demo.model.entities.FavDish

@Dao
interface FavDishDAO {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    
}