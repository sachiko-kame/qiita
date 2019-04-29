package com.example.qiitasampleproject.Api.Get

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface QiitaService {
    @GET("api/v2/users/{userName}/items")
    fun fetchReposList(@Path("userName") userName: String,  @Query("page") page: String): Call<List<UserRepos>>
}

data class UserRepos(val title: String,
                      val url: String,
                      val comments_count: Int,
                      val likes_count: Int,
                      val id: String
)

/*
*
*
* user Object
* tags Array
*
* */