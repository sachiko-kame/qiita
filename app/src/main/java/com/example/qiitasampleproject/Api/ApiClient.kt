package com.example.qiitasampleproject.Api

import com.example.qiitasampleproject.Api.Get.QiitaService
import com.example.qiitasampleproject.Api.Get.UserRepos
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    private const val BASE_URL = "https://qiita.com/"

    private fun restClient() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Github - Get
    fun fetchUserReposList(userName:String, page:String) : Response<List<UserRepos>> {
        val service = restClient().create(QiitaService::class.java)
        return service.fetchReposList(userName, page).execute()
    }

    fun fetchUser_allReposList() : Response<List<UserRepos>> {
        val service = restClient().create(QiitaService::class.java)
        return service.fetchReposList_all("1", "20").execute()
    }


    //Github - Post
}
