package com.galaxysoftware.istockenterpriseiorder.api

import com.galaxysoftware.istockenterpriseiorder.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //@GET("/search?media=podcast")
    @GET("/api/sync")
    fun getData(): Call<ResponseBody>

    @POST("/api/sync")
    fun setData(@Body data: ApiHelper) : Call<ResponseBody>
}