package com.radea.githubuser.data.retrofit

import com.radea.githubuser.data.response.ResponseDetailsUser
import com.radea.githubuser.data.response.ResponseUsersItem
import com.radea.githubuser.data.response.ResponseUsersSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getAllUsers(): Call<List<ResponseUsersItem>>

    @GET("search/users")
    fun getUsersByUsername(
        @Query("q") username: String
    ): Call<ResponseUsersSearch>

    @GET("users/{username}")
    fun getDetailsByUsername(
        @Path("username") username: String
    ): Call<ResponseDetailsUser>

    @GET("users/{username}/followers")
    fun getFollowersByUsername(
        @Path("username") username: String
    ): Call<List<ResponseUsersItem>>

    @GET("users/{username}/following")
    fun getFollowingByUsername(
        @Path("username") username: String
    ): Call<List<ResponseUsersItem>>
}