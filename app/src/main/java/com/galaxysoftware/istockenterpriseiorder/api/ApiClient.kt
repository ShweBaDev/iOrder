package com.galaxysoftware.istockenterpriseiorder.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        //"http://192.168.10.37:88/"  //"http://192.168.10.37:88/"   //http://192.168.1.6:88/   // http://13.212.177.143:89/  linux_aws_r&d

        private const val BASE_URL: String ="http://192.168.1.16:1314/"  //"http://13.212.177.143:88/"   //"http://13.212.177.143:88/"        //"http://192.168.100.6:88/"


        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
                .build()

        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}