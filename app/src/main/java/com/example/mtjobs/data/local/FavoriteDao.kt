package com.example.mtjobs.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mtjobs.data.models.FavoriteItem
import com.example.mtjobs.data.models.JobsResponseItem
import com.example.mtjobs.utils.Constants

@Dao
interface FavoriteDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFav(favoriteItem: FavoriteItem)

    @Delete()
    suspend fun deleteFav(favoriteItem: FavoriteItem)

    @Query("SELECT * FROM ${Constants.FAV_TABLE_NAME} ")
    fun getAllFav(): List<FavoriteItem>


}