package com.example.mtjobs.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mtjobs.data.models.FavoriteItem
import com.example.mtjobs.data.models.JobsResponseItem

@Database(entities = [JobsResponseItem::class , FavoriteItem::class], version = 1,exportSchema = false)
abstract class JobsDataBase : RoomDatabase() {
    abstract val jobsDao: JobsDao
    abstract val favoriteDao: FavoriteDao
}