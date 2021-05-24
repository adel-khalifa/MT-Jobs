package com.example.mtjobs.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mtjobs.data.models.JobsResponseItem
import com.example.mtjobs.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface JobsDao {
    @Query("SELECT * FROM ${Constants.JOBS_TABLE_NAME} ")
    fun findAll(): List<JobsResponseItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(jobs: List<JobsResponseItem>)
}