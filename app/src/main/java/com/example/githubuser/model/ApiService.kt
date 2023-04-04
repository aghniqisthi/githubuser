package com.example.githubuser.model

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users")
    fun getListUsers() : Call<List<ResponseDataUserItem>>

    @GET("search/users")
    fun searchUser(@Query("q") username: String) : Call<ResponseDataSearch>

    @GET("users/{username}")
    fun getDetailUsers(@Path("username") username: String) : Call<ResponseDataDetail>

    @GET("users/{username}/repos")
    fun getDetailRepo(@Path("username") username: String) : Call<List<ResponseDataReposItem>>

    @GET("users/{username}/followers")
    fun getDetailFollowers(@Path("username") username: String) : Call<List<ResponseDataUserItem>>

    @GET("users/{username}/following")
    fun getDetailFollowing(@Path("username") username: String) : Call<List<ResponseDataUserItem>>

}