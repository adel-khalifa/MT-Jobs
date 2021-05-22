package com.example.mtjobs.repo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mtjobs.data.models.JobsResponseItem
import com.example.mtjobs.utils.NetworkState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.launch
import java.io.IOException

class JobsViewModel(app: Application, private val jobsRepo: JobsRepoImpl) : AndroidViewModel(app) {

     var allJobs: MutableLiveData<NetworkState<List<JobsResponseItem>>> = MutableLiveData()
  //  var updateFavoritesLiveData: MutableLiveData<NetworkState<AuthResponse>> = MutableLiveData()

//    fun updateUserFavorites(trackID :String) {
//
//        viewModelScope.launch {
//            // force  initial Loading state
//            if (isConnected()) {
//                updateFavoritesLiveData.value = OnLoading()
//
//                try {
//                    // make a request via Repository
//                    val requestResult = mRepo.updateUserFavorites(trackID)
//                    if (requestResult.isSuccessful) {
//                        updateFavoritesLiveData.postValue(OnSuccess(requestResult.body()!!))
//                    } else {
//                        updateFavoritesLiveData.postValue(OnFailure(requestResult.message()))
//                    }
//                } catch (t: Throwable) {
//                    when (t) {
//                        is IOException -> handleConnectionFailed()
//                    }
//                }
//            } // if no internet connection
//            else handleNoInternetConnection()
//        }
//    }

    fun fetchCashedJobsFromDataBase() {
        viewModelScope.launch {
            jobsRepo.reloadJobs()

            jobsRepo.getAllJobsFromCash().collect {
                allJobs.postValue(NetworkState.OnSuccess(it))
            }


//            if (isConnected()) {
//                allJobs.value = NetworkState.OnLoading()
//
//                try {
//                    // make a request via Repository
//                    val requestResult = jobsRepo.favoriteRequest()
//                    if (requestResult.isSuccessful) {
//                            allJobs.postValue(OnSuccess(requestResult.body()!!))
//                    } else {
//                            allJobs.postValue(OnFailure(requestResult.message()))
//                    }
//                } catch (t: Throwable) {
//                    when (t) {
//                        is IOException -> handleConnectionFailed()
//                    }
//                }
//            } // if no internet connection
//            else handleNoInternetConnection()



        }
    }

//    private fun handleNoInternetConnection() {
//        val noInternetConnectionMessage =
//            getApplication<PaperSamWishApplication>().getString(R.string.no_internet_connection)
//        allJobs.postValue(OnFailure(noInternetConnectionMessage))
//    }
//
//    private fun handleConnectionFailed() {
//        val connectionFailedMessage =
//            getApplication<PaperSamWishApplication>().getString(R.string.connection_failed)
//        allJobs.postValue(OnFailure(connectionFailedMessage))
//    }



}