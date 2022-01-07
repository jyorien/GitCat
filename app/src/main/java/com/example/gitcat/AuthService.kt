package com.example.gitcat

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    @GET("start-oauth")
    fun getAuth(): Call<Any>

    @POST("repos")
    fun getRepos(@Body body: RequestBody): Call<List<Repository>>

    @POST("create-webhook")
    fun createWebhook(@Body body: RequestBody): Call<Any>

    companion object {
        private var INSTANCE: AuthService? = null
        fun getInstance(): AuthService {
            return INSTANCE ?: synchronized(this) {
                val client = OkHttpClient.Builder().build()
                val retrofit = Retrofit.Builder().client(client)
                    .baseUrl("https://gitcat.zectan.com/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                val service = retrofit.create(AuthService::class.java)
                INSTANCE = service
                service
            }
        }
    }
}