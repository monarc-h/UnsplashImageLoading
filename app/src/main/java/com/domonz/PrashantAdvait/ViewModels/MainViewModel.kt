package com.domonz.PrashantAdvait.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domonz.PrashantAdvait.DataModels.UnsplashApiRequest
import com.domonz.PrashantAdvait.DataModels.UnsplashPhoto
import com.domonz.PrashantAdvait.utils.NetworkResult
import com.domonz.softwarelabassignment.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val imageRepository: ImageRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<List<UnsplashPhoto>>>
        get() = imageRepository.userResponseLiveData


    fun getImages(request: UnsplashApiRequest) {
        viewModelScope.launch {
            imageRepository.getImages(request)
        }
    }


}