package com.rrat.mvvm2demo.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rrat.mvvm2demo.model.entities.FavDish

@Dao
interface FavDishDAO {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM  FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList() : kotlinx.coroutines.flow.Flow<List<FavDish>>

}