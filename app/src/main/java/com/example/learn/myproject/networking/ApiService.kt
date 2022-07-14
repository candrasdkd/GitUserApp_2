package com.example.learn.myproject.networking

import com.example.learn.myproject.datasource.SearchResponse
import com.example.learn.myproject.datasource.UserResponse
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getListUsersAsync(): ArrayList<UserResponse>

    @GET("users/{username}")
    suspend fun getDetailUserAsync(@Path("username") username: String): UserResponse

    @GET("search/users")
    fun getUserBySearch(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}/followers")
    suspend fun getListFollowers(@Path("username") username: String): ArrayList<UserResponse>

    @GET("users/{username}/following")
    suspend fun getListFollowing(@Path("username") username: String): ArrayList<UserResponse>
}
