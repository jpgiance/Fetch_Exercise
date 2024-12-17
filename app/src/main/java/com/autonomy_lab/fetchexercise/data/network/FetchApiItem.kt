package com.autonomy_lab.fetchexercise.data.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FetchApiItem(
    val id: Int?,
    val listId: Int?,
    val name: String?,
)
