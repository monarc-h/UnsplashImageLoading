package com.domonz.PrashantAdvait.api

import com.domonz.PrashantAdvait.DataModels.UnsplashPhoto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashAPI {
    @GET("/photos")
    suspend fun getPhotos(
        @Query("client_id") clientId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<UnsplashPhoto>>
}