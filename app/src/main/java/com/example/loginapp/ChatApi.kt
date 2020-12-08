package com.example.loginapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ChatApi {

    @Headers("Content-Type: application/json")
    @POST("authenticate.php")
    fun login(@Body userData: UserInfo): Call<LoginResponse>

    @DELETE("logout.php")
    @Headers("Content-Type: application/json")
    fun logout(@Header("Authorization") header: String): Call<Void>

    @Headers("Content-Type: application/json")
    @PUT("sendmessage.php")
    fun sendMessage(@Header("Authorization") header: String, @Body messageData: MessageData): Call<Void>

    @Headers("Content-Type: application/json")
    @GET("readmessages.php")
    fun readMessage(@Header("Authorization") header: String): Call<ReadMessagesResponse>

    companion object {
        fun create(): ChatApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://cgisdev.utcluj.ro/moodle/chat-piu/")
                .build()
            return retrofit.create(ChatApi::class.java)
        }
    }
}