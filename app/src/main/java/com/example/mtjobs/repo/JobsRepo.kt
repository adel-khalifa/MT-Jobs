package com.example.mtjobs.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.example.mtjobs.R
import com.example.mtjobs.data.local.FavoriteDao
import com.example.mtjobs.data.local.JobsDao
import com.example.mtjobs.data.models.FavoriteItem
import com.example.mtjobs.data.models.JobsResponse
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
    private val jobsDao: JobsDao,
    private val favoriteDao: FavoriteDao
) : JobsRepo {

    val localeJobsWithStatus: MutableLiveData<NetworkState<List<JobsResponseItem>>> =
        MutableLiveData()

    suspend fun addToFavorites(favoriteItem: FavoriteItem) = favoriteDao.addFav(favoriteItem)

    suspend fun deleteFromFavorites(favoriteItem: FavoriteItem) =
        favoriteDao.deleteFav(favoriteItem)

    suspend fun favoritesFlow(): Flow<List<FavoriteItem>> = withContext(Dispatchers.IO) {
        val allFav = favoriteDao.getAllFav()
        flow { emit(allFav) }
    }


    private suspend fun jobsFlow(): Flow<List<JobsResponseItem>> = withContext(Dispatchers.IO) {
        val localData = jobsDao.findAll()
        flow { emit(localData) }

    }


    override suspend fun forceRefreshLocalData() {

        if (isConnected()) {

            localeJobsWithStatus.value = NetworkState.OnLoading()

            try {
                val response = api.getJobs()
                if (response.isSuccessful) cashResponse(response.body())
                else setJobStateToFailure(response.message())


            } catch (e: Exception) {
                setJobStateToFailure(e.message.toString())
            }
        } else {
            jobsFlow().collect { cashedData ->
                if (cashedData.isEmpty()) setJobStateToFailure(context.getString(R.string.no_connection))
                else setJobStateToSuccess(cashedData)
            }
        }
    }

    private fun setJobStateToFailure(message: String) =
        localeJobsWithStatus.postValue(NetworkState.OnFailure(message))

    private fun setJobStateToSuccess(cashedJobs: List<JobsResponseItem>) =
        localeJobsWithStatus.postValue(NetworkState.OnSuccess(cashedJobs))


    private suspend fun cashResponse(body: JobsResponse?) =
        body?.let { jobsList ->
            withContext(Dispatchers.IO) {
                jobsDao.add(jobsList)
                jobsFlow().collect { cashedJobs ->
                    setJobStateToSuccess(cashedJobs)
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