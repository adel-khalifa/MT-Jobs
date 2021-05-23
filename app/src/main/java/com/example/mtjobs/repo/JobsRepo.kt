package com.example.mtjobs.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mtjobs.R
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
    suspend fun forceRefreshLocalData()
}

class JobsRepoImpl(
    private val api: JobsApi,
    private val context: Context,
    private val dao: JobsDao
) : JobsRepo {

    val localeJobsWithStatus: MutableLiveData<NetworkState<List<JobsResponseItem>>> =
        MutableLiveData()

    private suspend fun getAllJobsFromCash(): Flow<List<JobsResponseItem>> {
        return withContext(Dispatchers.IO) {
           val  localData = dao.findAll()
            flow { emit(localData) }

        }
    }

    override suspend fun forceRefreshLocalData() {
        Log.i("TAG", "reloadJobs: started")

        if (isConnected()) {

            Log.i("TAG", "reloadJobs: isConnected")
            localeJobsWithStatus.value=NetworkState.OnLoading()
            try {

                val response = api.getJobs()
                if (response.isSuccessful) {
                    Log.i("TAG", "reloadJobs: isSuccessful")

                    //cash into SQL
                    response.body()?.let { jobsList ->
                        withContext(Dispatchers.IO) {

                            // TODO try without casting the iterator
                            dao.add(jobsList.toList())

                            Log.i("TAG", "reloadJobs: add to DB")

                            getAllJobsFromCash().collect { cashedJobs ->
                                localeJobsWithStatus.postValue(NetworkState.OnSuccess(cashedJobs))
                            }
                        }
                    }
                } else {
                    localeJobsWithStatus.postValue(NetworkState.OnFailure(response.message()))
                    Log.i("TAG", "reloadJobs: response ******UnSuccessful*****")
                }
            } catch (e: Exception) {
                Log.i("TAG", "reloadJobs: ******** Exception ******${e.message}")
                localeJobsWithStatus.postValue(NetworkState.OnFailure(e.message.toString()))

            }
        } else {
            //check in db if the data exists
            val data = getAllJobsFromCash().collect { cashedData ->
                if (cashedData.isEmpty()) {
                    localeJobsWithStatus.postValue(NetworkState.OnFailure(context.getString(R.string.no_connection)))

                } else {
                    localeJobsWithStatus.postValue(NetworkState.OnSuccess(cashedData))

                }
            }


        }
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