package com.example.mtjobs.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mtjobs.utils.Constants

@Entity(tableName = Constants.FAV_TABLE_NAME)
class FavoriteItem(
    @PrimaryKey val id: String,
    //@ColumnInfo(name = "job_id") val jobId: String
)