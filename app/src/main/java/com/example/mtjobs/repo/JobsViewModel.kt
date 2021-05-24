package com.example.mtjobs.repo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mtjobs.data.models.FavoriteItem
import kotlinx.coroutines.flow.collect

import kotlinx.coroutines.launch

class JobsViewModel(app: Application, private val jobsRepo: JobsRepoImpl) : AndroidViewModel(app) {

    init {
            reloadData()
            loadFavorites()
    }

    val cashedJobs= jobsRepo.localeJobsWithStatus

    fun reloadData() = viewModelScope.launch { jobsRepo.forceRefreshLocalData() }


    val favoritesLiveData: MutableLiveData<List<FavoriteItem>> = MutableLiveData()

    private fun loadFavorites() = viewModelScope.launch {
        jobsRepo.favoritesFlow().collect {
            favoritesLiveData.value = it
        }
    }


    fun toggleIsFavorite(favoriteItem: FavoriteItem) = viewModelScope.launch {

        jobsRepo.favoritesFlow().collect { favFlow ->
            if (favFlow.any { it.id == favoriteItem.id })
                jobsRepo.deleteFromFavorites(favoriteItem)
            else jobsRepo.addToFavorites(favoriteItem)

        }

        jobsRepo.favoritesFlow().collect { favoritesLiveData.postValue(it) }

    }

}
