package com.autonomy_lab.fetchexercise.network

import com.autonomy_lab.fetchexercise.data.network.FetchApiItem
import retrofit2.Response
import retrofit2.http.GET

interface FetchApi {

    @GET("hiring.json")
    suspend fun fetchItemList(): Response<List<FetchApiItem>>
}