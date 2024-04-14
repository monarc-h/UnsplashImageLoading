package com.domonz.PrashantAdvait.DataModels

import com.google.gson.annotations.SerializedName



data class UnsplashPhoto(
    val id: String,
    val urls: UnsplashUrls
)

data class UnsplashUrls(
    @SerializedName("thumb") val thumbUrl: String,
    @SerializedName("regular") val regularUrl: String,
)