package com.example.newsapp.api

import com.example.newsapp.model.NewsResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines/{country}/{category}/")
    fun getArticles(@Path("country")country:String,@Path("category")category: String,@Query("q")searchKey:String):Single<NewsResponse>
}