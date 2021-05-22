package com.example.mtjobs.data.remote

import com.example.mtjobs.data.models.JobsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JobsApi {


    @GET("/positions.json?description=api")
    suspend fun getJobs(): Response<JobsResponse>


}