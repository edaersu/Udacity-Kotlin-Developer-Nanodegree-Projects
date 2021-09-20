package com.example.videos.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.videos.database.getDatabase
import com.example.videos.repository.VideosRepository

import kotlinx.coroutines.launch

class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val videosRepository = VideosRepository(database)

    init {
        viewModelScope.launch {
            videosRepository.refreshVideos()
        }
    }

    val playlist = videosRepository.videos
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
