package com.example.mtjobs.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.mtjobs.data.local.JobsDao
import com.example.mtjobs.data.models.JobsResponseItem
import com.example.mtjobs.data.remote.JobsApi
import com.example.mtjobs.utils.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface JobsRepo {
    suspend fun reloadJobs()
}

class JobsRepoImpl(
    private val api: JobsApi,
    private val context: Context,
    private val dao: JobsDao
) : JobsRepo {

private var localData : List<JobsResponseItem> = listOf()
    suspend fun getAllJobsFromCash(): Flow<List<JobsResponseItem>> {
        return withContext(Dispatchers.IO) {
             localData = dao.findAll()
            flow { emit(localData) }

        }
    }

    override suspend fun reloadJobs() {
        Log.i("TAG", "reloadJobs: started")

        if (isConnected()) {
            Log.i("TAG", "reloadJobs: isConnected")

            try {

                val response = api.getJobs()
                if (response.isSuccessful) {
                    Log.i("TAG", "reloadJobs: isSuccessful")

                    //cash into SQL
                    response.body()?.let {
                        withContext(Dispatchers.IO) {
                            dao.add(it.toList())
                            Log.i("TAG", "reloadJobs: add to DB")

                        }
                    }

//                    getAllJobsFromCash().collect {
//                         NetworkState.OnSuccess(it)
//                    }
                    // collected

                } else {
                    Log.i("TAG", "reloadJobs: response ******UnSuccessful*****")
                    return
                }
            } catch (e: Exception) {
                Log.i("TAG", "reloadJobs: ******** Exception ******${e.message}")
            }
        }
//        else {
//            //check in db if the data exists
//            val data = getAllJobsFromCash()
//            return if (data.isNotEmpty()) {
//                Log.d(TAG, "from db")
//                AppResult.Success(data)
//            } else
//            //no network
//                context.noNetworkConnectivityError()
//        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}