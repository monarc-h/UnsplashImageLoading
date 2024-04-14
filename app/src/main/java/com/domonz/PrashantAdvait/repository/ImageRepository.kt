package com.domonz.softwarelabassignment.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.domonz.PrashantAdvait.App
import com.domonz.PrashantAdvait.DataModels.UnsplashApiRequest
import com.domonz.PrashantAdvait.DataModels.UnsplashPhoto
import com.domonz.PrashantAdvait.R
import com.domonz.PrashantAdvait.api.UnsplashAPI
import com.domonz.PrashantAdvait.utils.Constants
import com.domonz.PrashantAdvait.utils.NetworkResult

import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class ImageRepository @Inject constructor(private val unsplashAPI: UnsplashAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<List<UnsplashPhoto>>>()
    val userResponseLiveData: LiveData<NetworkResult<List<UnsplashPhoto>>>
        get() = _userResponseLiveData

    suspend fun getImages(request: UnsplashApiRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        try {
            val response = unsplashAPI.getPhotos(request.clientId, request.page, request.perPage)
            handleResponse(response)
        }catch (e:Exception){
            val message = if(Constants.isNetworkAvailable(App.instance!!)){
                "Something went wrong please try again later."
            }else{
                "Looks like your network is disabled, please turn on to continue exploring"
            }
            _userResponseLiveData.postValue(NetworkResult.Error(message))

        }
    }


    private fun handleResponse(response: Response<List<UnsplashPhoto>>) {
        try {
            if (response.isSuccessful && response.body() != null) {
                Log.e(Constants.TAG, response.body().toString())
                _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else if (response.code() != 200) {
                if(response.code() == 401){
                    _userResponseLiveData.postValue(NetworkResult.PermissionError("Your access token is expired or invalid."))
                }else if(response.code() == 403){
                    _userResponseLiveData.postValue(NetworkResult.PermissionError("Looks like your request limit is on the peak, please try again later."))
                }else{
                    _userResponseLiveData.postValue(NetworkResult.PermissionError("The requested resource didn't found"))
                }
            } else {
                _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        }catch (e:Exception){
            println("Error message: $e")
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

}